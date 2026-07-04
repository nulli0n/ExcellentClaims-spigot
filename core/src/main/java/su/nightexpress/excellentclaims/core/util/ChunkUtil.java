package su.nightexpress.excellentclaims.core.util;

import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public final class ChunkUtil {

    private ChunkUtil() {
    }

    public static long getChunkKey(Chunk chunk) {
        return getChunkKey(chunk.getX(), chunk.getZ());
    }

    public static long getChunkKey(ChunkPos pos) {
        return getChunkKey(pos.getX(), pos.getZ());
    }

    public static long getChunkKey(int chunkX, int chunkZ) {
        return chunkX & 0xFFFFFFFFL | (chunkZ & 0xFFFFFFFFL) << 32;
    }

    public static long getChunkKeyOfBlock(Block block) {
        return getChunkKey(block.getX() >> 4, block.getZ() >> 4);
    }

    public static int getRelativeChunkPosition(Block block) {
        int relX = (block.getX() % 16 + 16) % 16;
        int relZ = (block.getZ() % 16 + 16) % 16;
        int relY = block.getY();
        return (relY & 0xFFFF) | ((relX & 0xFF) << 16) | ((relZ & 0xFF) << 24);
    }
}
