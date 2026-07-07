package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class CaveVinesGrowRule extends BaseBlockGrowRule {

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return context.newState().getType() == Material.CAVE_VINES;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Cave Vine Grow")
            .description(
                "Controls whether cave vines",
                "can grow here.",
                "",
                "(does not affect berry generation)"
            )
            .icon(Material.GLOW_BERRIES)
            .build();
    }
}
