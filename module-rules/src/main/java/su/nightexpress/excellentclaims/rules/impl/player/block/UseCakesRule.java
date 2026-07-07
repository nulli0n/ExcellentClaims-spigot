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
public class UseCakesRule extends BasePlayerUseBlockRule {

    public UseCakesRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isCake(Material material) {
        return material == Material.CAKE || Tag.CANDLE_CAKES.isTagged(material);
    }

    @Override
    protected boolean shouldHandle(BlockInteractContext context, Block block) {
        return this.isCake(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Cakes")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can eat placed cakes."
            )
            .icon(Material.CAKE)
            .build();
    }
}
