package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class SugarCaneGrowRule extends BaseBlockGrowRule {

    public SugarCaneGrowRule(boolean resetBlockAge) {
        super(resetBlockAge, BlockFace.UP);
    }

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return event.getNewState().getType() == Material.SUGAR_CANE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Sugar Cane Grow")
            .description("Controls whether sugar cane",
                "can grow here."
            )
            .icon(Material.SUGAR_CANE)
            .build();
    }
}
