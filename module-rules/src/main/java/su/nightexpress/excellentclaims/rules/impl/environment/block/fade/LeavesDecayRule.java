package su.nightexpress.excellentclaims.rules.impl.environment.block.fade;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFadeContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class LeavesDecayRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeContext context) {
        return Tag.LEAVES.isTagged(context.block().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Leaves Decay")
            .description("Controls whether leaves can",
                "decay here."
            )
            .icon(Material.OAK_LEAVES)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
