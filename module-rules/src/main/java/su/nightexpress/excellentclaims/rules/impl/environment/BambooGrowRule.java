package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowAsSpreadRule;

@NullMarked
public class BambooGrowRule extends BaseBlockGrowAsSpreadRule {

    public BambooGrowRule() {
        super(true, BlockFace.UP);
    }

    @Override
    protected boolean shouldHandle(BlockSpreadEvent event) {
        return event.getNewState().getType() == Material.BAMBOO;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Bamboo Grow")
            .description("Controls whether bamboo can",
                "grow here."
            )
            .icon(Material.BAMBOO)
            .build();
    }

}
