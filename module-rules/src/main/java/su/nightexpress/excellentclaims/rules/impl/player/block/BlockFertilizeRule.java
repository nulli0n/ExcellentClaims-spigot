package su.nightexpress.excellentclaims.rules.impl.player.block;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BlockFertilizeRule extends SimpleSpec<BlockFertilizeEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BlockFertilizeRule(ClaimPermissionAPI permissions) {
        super(BlockFertilizeEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<BlockFertilizeEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .weight(10)
            .shouldHandle(event -> true)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getBlock()))
            .playerExtractor(BlockFertilizeEvent::getPlayer)
            .trigger((event, registry, origin, rule, allowed) -> {
                Player player = event.getPlayer();
                if (this.permissions.hasPermission(player, origin, ClaimPermission.BUILDING)) {
                    return RuleResult.pass();
                }

                if (!allowed) {
                    Block block = event.getBlock();
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_BLOCK_FERTILIZE, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
                    ));
                }

                List<BlockState> affectedBlocks = event.getBlocks();

                affectedBlocks.removeIf(state -> {
                    Claim nextClaim = registry.getPrioritizedClaim(state.getLocation());
                    if (nextClaim == null || nextClaim == origin) return false;

                    if (this.permissions.hasPermission(player, nextClaim, ClaimPermission.BUILDING)) {
                        return false;
                    }

                    Boolean nextState = nextClaim.getRuleOrIgnoreIfUnset(rule).orElse(null);
                    return nextState != null && !nextState;
                });

                return affectedBlocks.isEmpty() ? RuleResult.deny() : RuleResult.allow();
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
