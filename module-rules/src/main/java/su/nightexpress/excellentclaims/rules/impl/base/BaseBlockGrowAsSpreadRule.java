package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
/**
 * A class for rules for blocks that uses BlockSpreadEvent, but acts like normal growing.
 */
public abstract class BaseBlockGrowAsSpreadRule extends AbstractBlockGrowRule<BlockSpreadEvent> {

    public BaseBlockGrowAsSpreadRule(boolean resetBlockAge, BlockFace growDirection) {
        super(BlockSpreadEvent.class, resetBlockAge, growDirection);
    }

    public BaseBlockGrowAsSpreadRule(boolean resetBlockAge, BlockFace growDirection, int weight) {
        super(BlockSpreadEvent.class, resetBlockAge, growDirection, weight);
    }

    @Override
    protected Block getSourceBlock(BlockSpreadEvent event) {
        return event.getSource();
    }
}
