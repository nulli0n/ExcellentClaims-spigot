package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BlockTrampRule extends BaseEntityBlockChangeRule {

    public BlockTrampRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isTrampable(Material material) {
        return material == Material.TURTLE_EGG || material == Material.FARMLAND;
    }

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity event, Block block) {
        return context.actor() != null && this.isTrampable(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Block Trample")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can trample crops & eggs."
            )
            .icon(Material.WHEAT_SEEDS)
            .build();
    }
}
