package su.nightexpress.excellentclaims.rules.impl.player;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class FillBucketRule extends SimpleSpec<BlockInteractContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public FillBucketRule(ClaimPermissionAPI permissions) {
        super(BlockInteractContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<BlockInteractContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.isFluid(context.block())) // Handle only fluid interactions
            .process((event, registry, context) -> {
                Claim claim = registry.getPrioritizedClaim(event.block());
                if (claim == null) return RuleResult.allow();

                Player player = event.actor();
                if (this.permissions.hasPermission(player, claim, ClaimPermission.BUILDING)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    private boolean isFluid(Block block) {
        if (!(block.getBlockData() instanceof Levelled)) return false;

        return block.getType() == Material.WATER || block.getType() == Material.LAVA;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Fill Buckets")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Building") + " permission",
                "can fill buckets here."
            )
            .icon(Material.BUCKET)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
