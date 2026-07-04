package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class MyceliumSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.MYCELIUM;
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
