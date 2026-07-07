package su.nightexpress.excellentclaims.rules.impl.environment.block.grow;

import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class CropGrowRule extends BaseBlockGrowRule {

    public CropGrowRule() {
        super(10);
    }

    @Override
    protected boolean shouldHandle(BlockGrowContext context) {
        return context.sourceBlock().getBlockData() instanceof Ageable;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Crop Grow")
            .description(
                "Controls whether crops and",
                "plants can grow here.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.WHEAT)
            .build();
    }

}
