package su.nightexpress.excellentclaims.rules.impl.environment.block.spread;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class AmethystSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadContext context) {
        return context.newState().getType() == Material.SMALL_AMETHYST_BUD;
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
