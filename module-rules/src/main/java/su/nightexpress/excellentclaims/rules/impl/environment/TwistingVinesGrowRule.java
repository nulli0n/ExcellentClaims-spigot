package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowAsSpreadRule;

@NullMarked
public class TwistingVinesGrowRule extends BaseBlockGrowAsSpreadRule {

    public TwistingVinesGrowRule() {
        super(false, BlockFace.UP); // Do not reset age since it works differently
    }

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.TWISTING_VINES;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Twisting Vines Grow")
            .description(
                "Controls whether twisting vines",
                "can grow here."
            )
            .icon(Material.TWISTING_VINES)
            .build();
    }
}
