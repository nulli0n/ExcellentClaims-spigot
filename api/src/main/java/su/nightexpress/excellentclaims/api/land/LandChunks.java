package su.nightexpress.excellentclaims.api.land;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public interface LandChunks {

    Set<ChunkPos> getChunkPositions();

    int size();

    boolean contains(ChunkPos chunkPos);

    boolean isEmpty();

    void addChunkPosition(ChunkPos pos);

    void removeChunkPosition(ChunkPos pos);
}
