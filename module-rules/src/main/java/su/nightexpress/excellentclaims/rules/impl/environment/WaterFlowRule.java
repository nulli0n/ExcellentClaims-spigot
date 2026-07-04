package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFromToEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFromToRule;

@NullMarked
public class WaterFlowRule extends BaseBlockFromToRule {

    @Override
    protected boolean shouldHandle(BlockFromToEvent event) {
        return event.getBlock().getType() == Material.WATER;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Water Flow")
            .description(
                "Controls whether water can",
                "flow into this claim from",
                "outside."
            )
            .icon(Material.WATER_BUCKET)
            .build();
    }
}
