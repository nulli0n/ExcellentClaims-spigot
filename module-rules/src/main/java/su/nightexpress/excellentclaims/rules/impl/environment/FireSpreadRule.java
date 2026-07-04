package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class FireSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.FIRE;
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
