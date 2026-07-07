package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class BambooGrowRule extends BaseBlockGrowRule {

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return context.newState().getType() == Material.BAMBOO;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Bamboo Grow")
            .description("Controls whether bamboo can",
                "grow here."
            )
            .icon(Material.BAMBOO)
            .build();
    }

}
