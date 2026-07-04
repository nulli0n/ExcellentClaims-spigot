package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFormRule;

@NullMarked
public class SnowFormRule extends BaseBlockFormRule {

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return event.getNewState().getType() == Material.SNOW;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Snow Form")
            .description("Controls whether snow can",
                "form during snowfall here."
            )
            .icon(Material.SNOW)
            .build();
    }
}
