package su.nightexpress.excellentclaims.rules.impl.environment.block.spread;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class GrassSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadContext context) {
        return context.newState().getType() == Material.GRASS_BLOCK;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Grass Spread")
            .description("Controls whether grass can",
                "spread on dirt blocks."
            )
            .icon(Material.GRASS_BLOCK)
            .build();
    }
}
