package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFormRule;

@NullMarked
public class IceFormRule extends BaseBlockFormRule {

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return event.getNewState().getType() == Material.ICE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Ice Form")
            .description("Controls whether ice can",
                "form here."
            )
            .icon(Material.ICE)
            .build();
    }
}
