package su.nightexpress.excellentclaims.rules.impl.environment.block.fade;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFadeContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class CoralDieRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeContext context) {
        Material type = context.block().getType();
        return Tag.CORALS.isTagged(type) || Tag.CORAL_BLOCKS.isTagged(type);
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Coral Die")
            .description(
                "Controls whether corals can",
                "die here."
            )
            .icon(Material.BRAIN_CORAL)
            .build();
    }
}
