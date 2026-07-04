package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowAsSpreadRule;

@NullMarked
public class WeepingVinesGrowRule extends BaseBlockGrowAsSpreadRule {

    public WeepingVinesGrowRule() {
        super(false, BlockFace.DOWN); // Do not reset age since it works differently
    }

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.WEEPING_VINES;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Weeping Vines Grow")
            .description(
                "Controls whether weeping vines",
                "can grow here."
            )
            .icon(Material.WEEPING_VINES)
            .build();
    }
}
