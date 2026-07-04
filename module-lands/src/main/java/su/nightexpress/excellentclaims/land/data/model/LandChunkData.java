package su.nightexpress.excellentclaims.land.data.model;

import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.land.LandChunks;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class LandChunkData implements LandChunks {

    private final Set<ChunkPos> chunkPositions;

    public LandChunkData() {
        this(Set.of());
    }

    public LandChunkData(Set<ChunkPos> chunkPositions) {
        this.chunkPositions = new HashSet<>(chunkPositions);
    }

    @Override
    public Set<ChunkPos> getChunkPositions() {
        return chunkPositions;
    }

    @Override
    public int size() {
        return this.chunkPositions.size();
    }

    @Override
    public boolean contains(ChunkPos chunkPos) {
        return this.chunkPositions.contains(chunkPos);
    }

    @Override
    public boolean isEmpty() {
        return this.chunkPositions.isEmpty();
    }

    @Override
    public void addChunkPosition(ChunkPos pos) {
        this.chunkPositions.add(pos);
    }

    @Override
    public void removeChunkPosition(ChunkPos pos) {
        this.chunkPositions.remove(pos);
    }
}
