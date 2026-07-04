package su.nightexpress.excellentclaims.api.land;

import java.util.Set;

import org.bukkit.Chunk;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public interface Land extends OwnableClaim {

    boolean contains(ChunkPos chunkPos);

    boolean isMerged();

    boolean isSingle();

    int getChunksAmount();

    Set<ChunkPos> getPositions();

    Set<Chunk> getChunks();

    LandChunks getChunkData();
}
