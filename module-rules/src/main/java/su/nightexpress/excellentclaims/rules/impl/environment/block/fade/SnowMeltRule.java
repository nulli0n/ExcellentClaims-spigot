package su.nightexpress.excellentclaims.rules.impl.environment.block.fade;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFadeContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class SnowMeltRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeContext context) {
        Material type = context.block().getType();
        return type == Material.SNOW; // Only layers can smelt
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Snow Melt")
            .description(
                "Controls whether snow can",
                "melt here."
            )
            .icon(Material.SNOW_BLOCK)
            .build();
    }
}
