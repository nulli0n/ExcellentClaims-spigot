package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UsePlatesRule extends BasePlayerUseBlockRule {

    public UsePlatesRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isPlate(Material material) {
        return Tag.PRESSURE_PLATES.isTagged(material);
    }

    @Override
    protected boolean shouldHandle(BlockInteractContext context, Block block) {
        Action action = context.action();

        return action == Action.PHYSICAL && this.isPlate(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Plates")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can use pressure plates."
            )
            .icon(Material.STONE_PRESSURE_PLATE)
            .build();
    }
}
