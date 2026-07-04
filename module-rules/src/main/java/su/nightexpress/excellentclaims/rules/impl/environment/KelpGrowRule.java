package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowAsSpreadRule;

@NullMarked
public class KelpGrowRule extends BaseBlockGrowAsSpreadRule {

    public KelpGrowRule() {
        super(false, BlockFace.UP); // Do not reset kelp's age since it works differently
    }

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.KELP;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Kelp Grow")
            .description("Controls whether kelp can",
                "grow here."
            )
            .icon(Material.KELP)
            .build();
    }
}
