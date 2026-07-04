package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFromToEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFromToRule;

@NullMarked
public class LavaFlowRule extends BaseBlockFromToRule {

    @Override
    protected boolean shouldHandle(BlockFromToEvent event) {
        return event.getBlock().getType() == Material.LAVA;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Lava Flow")
            .description(
                "Controls whether lava can",
                "flow into this claim from",
                "outside."
            )
            .icon(Material.LAVA_BUCKET)
            .build();
    }
}
