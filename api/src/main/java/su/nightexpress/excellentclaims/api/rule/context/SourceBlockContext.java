package su.nightexpress.excellentclaims.api.rule.context;

import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface SourceBlockContext {

    Block sourceBlock();
}
