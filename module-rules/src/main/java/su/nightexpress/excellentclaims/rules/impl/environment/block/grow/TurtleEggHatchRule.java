package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class TurtleEggHatchRule extends BaseBlockGrowRule {

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return context.newState().getType() == Material.TURTLE_EGG;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Turtle Egg Hatch")
            .description("Controls whether turtle eggs",
                "can hatch here."
            )
            .icon(Material.TURTLE_EGG)
            .build();
    }
}
