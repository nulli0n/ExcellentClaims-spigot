package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.block.BlockState;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface BlockStateContext {

    BlockState blockState();
}
