package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class CropGrowRule extends BaseBlockGrowRule {

    public CropGrowRule(boolean resetBlockAge) {
        super(resetBlockAge, BlockFace.SELF, 10); // Generic rule, add more weight
    }

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return event.getBlock().getBlockData() instanceof Ageable;
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
