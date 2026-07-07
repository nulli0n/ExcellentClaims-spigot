package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class CactusGrowRule extends BaseBlockGrowRule {

    private static final Set<Material> CACTUSES = Set.of(
        Material.CACTUS,
        Material.CACTUS_FLOWER
    );

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return CACTUSES.contains(context.newState().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Cactus Grow")
            .description("Controls whether cactus can",
                "grow here."
            )
            .icon(Material.CACTUS)
            .build();
    }
}
