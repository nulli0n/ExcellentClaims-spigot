package su.nightexpress.excellentclaims.rules.impl.environment.block.fromto;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFromToContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFromToRule;

@NullMarked
public class LavaFlowRule extends BaseBlockFromToRule {

    @Override
    protected boolean shouldHandle(BlockFromToContext context) {
        return context.sourceBlock().getType() == Material.LAVA;
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
