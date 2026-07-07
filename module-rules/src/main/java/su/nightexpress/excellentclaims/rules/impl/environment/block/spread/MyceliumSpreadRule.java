package su.nightexpress.excellentclaims.rules.impl.environment.block.spread;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class MyceliumSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadContext context) {
        return context.newState().getType() == Material.MYCELIUM;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Mycelium Spread")
            .description("Controls whether mycelium can",
                "spread here."
            )
            .icon(Material.MYCELIUM)
            .build();
    }
}
