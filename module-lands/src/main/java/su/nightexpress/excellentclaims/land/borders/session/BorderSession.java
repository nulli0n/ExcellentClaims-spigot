package su.nightexpress.excellentclaims.land.borders.session;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import su.nightexpress.excellentclaims.api.highlight.EntityReference;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

public class BorderSession {

    private final Map<ChunkPos, List<EntityReference>> entityMap;

    public BorderSession() {
        this.entityMap = new ConcurrentHashMap<>();
    }

    public boolean hasChunk(ChunkPos chunkPos) {
        return this.entityMap.containsKey(chunkPos);
    }

    public Set<ChunkPos> getChunks() {
        return Set.copyOf(this.entityMap.keySet());
    }

    public List<EntityReference> getEntities() {
        return this.entityMap.values().stream().flatMap(List::stream).toList();
    }

    public List<EntityReference> getEntities(ChunkPos chunkPos) {
        return this.entityMap.getOrDefault(chunkPos, List.of());
    }

    public void addReference(ChunkPos pos, EntityReference reference) {
        this.entityMap.computeIfAbsent(pos, k -> new CopyOnWriteArrayList<>()).add(reference);
    }

    public void clear(ChunkPos chunkPos) {
        this.entityMap.remove(chunkPos);
    }

    public void clearReferences() {
        this.entityMap.clear();
    }
}
