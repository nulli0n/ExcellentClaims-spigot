package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class VineGrowRule extends BaseBlockGrowRule {

    private static final Set<Material> VINES = Set.of(
        Material.VINE,
        Material.CAVE_VINES
    );

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return VINES.contains(context.newState().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Vine Grow")
            .description("Controls whether vines can",
                "grow (on the same block) here."
            )
            .icon(Material.VINE)
            .build();
    }
}
