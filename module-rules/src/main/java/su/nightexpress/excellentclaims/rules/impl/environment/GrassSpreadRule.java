package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class GrassSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.GRASS_BLOCK;
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
