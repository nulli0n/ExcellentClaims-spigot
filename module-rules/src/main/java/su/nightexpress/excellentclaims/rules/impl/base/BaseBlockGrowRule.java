package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class BaseBlockGrowRule extends AbstractBlockGrowRule<BlockGrowEvent> {

    public BaseBlockGrowRule(boolean resetBlockAge, BlockFace growDirection) {
        this(resetBlockAge, growDirection, 0);
    }

    public BaseBlockGrowRule(boolean resetBlockAge, BlockFace growDirection, int weight) {
        super(BlockGrowEvent.class, resetBlockAge, growDirection, weight);
    }
}
