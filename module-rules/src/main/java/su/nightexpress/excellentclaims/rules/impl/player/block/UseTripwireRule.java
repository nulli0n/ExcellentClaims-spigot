package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseTripwireRule extends BasePlayerUseBlockRule {

    public UseTripwireRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isTripwire(Material material) {
        return material == Material.TRIPWIRE;
    }

    @Override
    protected boolean shouldHandle(BlockInteractContext context, Block block) {
        return context.action() == Action.PHYSICAL && this.isTripwire(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Tripwire")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can trigger tripwires."
            )
            .icon(Material.TRIPWIRE_HOOK)
            .build();
    }
}
