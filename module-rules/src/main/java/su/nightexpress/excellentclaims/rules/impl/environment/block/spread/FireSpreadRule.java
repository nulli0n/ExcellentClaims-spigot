package su.nightexpress.excellentclaims.rules.impl.environment.block.spread;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class FireSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadContext context) {
        return context.newState().getType() == Material.FIRE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Fire Spread")
            .description("Controls whether fire can",
                "spread here."
            )
            .icon(Material.FLINT_AND_STEEL)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
