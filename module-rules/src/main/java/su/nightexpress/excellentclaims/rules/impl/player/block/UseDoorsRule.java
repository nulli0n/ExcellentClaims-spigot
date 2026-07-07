package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseDoorsRule extends BasePlayerUseBlockRule {

    public UseDoorsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isDoor(Material material) {
        return Tag.DOORS.isTagged(material) || Tag.TRAPDOORS.isTagged(material) || Tag.FENCE_GATES.isTagged(material);
    }

    @Override
    protected boolean shouldHandle(BlockInteractContext context, Block block) {
        return this.isDoor(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Doors")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can use doors."
            )
            .icon(Material.OAK_DOOR)
            .build();
    }
}
