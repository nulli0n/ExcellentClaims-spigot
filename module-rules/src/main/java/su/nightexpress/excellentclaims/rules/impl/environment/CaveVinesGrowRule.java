package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowAsSpreadRule;

@NullMarked
public class CaveVinesGrowRule extends BaseBlockGrowAsSpreadRule {

    public CaveVinesGrowRule() {
        super(false, BlockFace.DOWN); // Do not reset age since it works differently
    }

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.CAVE_VINES;
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
