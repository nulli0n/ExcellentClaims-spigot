package su.nightexpress.excellentclaims.api.highlight;

import org.bukkit.block.data.BlockData;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public record BlockContext(ExactPos position, BlockData data) {

}
