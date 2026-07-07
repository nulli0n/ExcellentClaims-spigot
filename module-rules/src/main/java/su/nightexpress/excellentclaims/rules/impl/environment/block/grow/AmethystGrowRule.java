package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;
import su.nightexpress.nightcore.util.Lists;

@NullMarked
public class AmethystGrowRule extends BaseBlockGrowRule {

    private static final Set<Material> AMETHYSTS = Lists.newSet(
        Material.SMALL_AMETHYST_BUD,
        Material.MEDIUM_AMETHYST_BUD,
        Material.LARGE_AMETHYST_BUD,
        Material.AMETHYST_CLUSTER
    );

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return AMETHYSTS.contains(context.newState().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Amethyst Grow")
            .description("Controls whether amethysts can",
                "grow here."
            )
            .icon(Material.MEDIUM_AMETHYST_BUD)
            .build();
    }
}
