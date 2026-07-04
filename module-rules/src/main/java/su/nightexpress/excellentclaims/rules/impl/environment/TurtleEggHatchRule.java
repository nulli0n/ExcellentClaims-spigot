package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class TurtleEggHatchRule extends BaseBlockGrowRule {

    public TurtleEggHatchRule(boolean resetBlockAge) {
        super(resetBlockAge, BlockFace.SELF);
    }

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return event.getNewState().getType() == Material.TURTLE_EGG;
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
