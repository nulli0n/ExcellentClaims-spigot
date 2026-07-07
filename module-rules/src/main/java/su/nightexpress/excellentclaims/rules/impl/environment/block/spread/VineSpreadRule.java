package su.nightexpress.excellentclaims.rules.impl.environment.block.spread;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class VineSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadContext context) {
        return context.newState().getType() == Material.VINE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Vine Spread")
            .description(
                "Controls whether vines can",
                "spread (on nearby blocks) here."
            )
            .icon(Material.VINE)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
