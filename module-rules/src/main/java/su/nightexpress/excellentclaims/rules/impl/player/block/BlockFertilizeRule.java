package su.nightexpress.excellentclaims.rules.impl.player.block;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFertilizeContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BlockFertilizeRule extends SimpleSpec<BlockFertilizeContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BlockFertilizeRule(ClaimPermissionAPI permissions) {
        super(BlockFertilizeContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<BlockFertilizeContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(10)
            .shouldHandle(event -> true)
            .process((context, registry, resolver) -> {
                Player player = context.actor();
                BlockState blockState = context.blockState();

                Claim claim = registry.getPrioritizedClaim(blockState.getLocation());
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, ClaimPermission.BUILDING)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> sourceState = resolver.resolveValue(claim);
                if (sourceState.isPresent() && !sourceState.get()) {
                    return RuleResult.deny();
                }


                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Block Fertilize")
            .description(
                "Controls whether outsiders and",
                "members without the" + TagWrappers.WHITE.wrap("Building"),
                "claim permission can",
                "fertilize blocks."
            )
            .icon(Material.BONE_MEAL)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
