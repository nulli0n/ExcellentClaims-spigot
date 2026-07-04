package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockSpreadRule;

@NullMarked
public class VineSpreadRule extends BaseBlockSpreadRule {

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.VINE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Vine Spread")
            .description(
                "Controls whether vines can",
                "spread (on nearby blocks) here."
            )
            .icon(Material.VINE)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
