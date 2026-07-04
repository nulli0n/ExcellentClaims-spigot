package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class AmethystSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.SMALL_AMETHYST_BUD;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Amethyst Spread")
            .description(
                "Controls whether budding",
                "amethysts can spawn amethyst buds."
            )
            .icon(Material.SMALL_AMETHYST_BUD)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
