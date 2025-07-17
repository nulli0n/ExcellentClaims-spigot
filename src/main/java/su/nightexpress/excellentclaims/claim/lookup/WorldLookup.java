package su.nightexpress.excellentclaims.claim.lookup;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

import java.util.*;
import java.util.stream.Collectors;

public class WorldLookup<T extends Claim> {

    private final Map<String, T>        byIdMap;
    private final Map<ChunkPos, Set<T>> byChunkPosMap;
    private final Map<UUID, Set<T>>     byOwnerIdMap;

    public WorldLookup() {
        this.byIdMap = new HashMap<>();
        this.byChunkPosMap = new HashMap<>();
        this.byOwnerIdMap = new HashMap<>();
    }

    public void update(@NotNull T claim) {
        this.purge(claim);
        this.store(claim);
    }

    public void store(@NotNull T claim) {
        this.byIdMap.put(claim.getId(), claim);

        claim.getEffectiveChunkPositions().forEach(chunkPos -> {
            this.byChunkPosMap.computeIfAbsent(chunkPos, k -> new HashSet<>()).add(claim);
        });

        this.byOwnerIdMap.computeIfAbsent(claim.getOwnerId(), k -> new HashSet<>()).add(claim);
    }

    public void purge(@NotNull T claim) {
        this.byIdMap.remove(claim.getId());

        claim.getEffectiveChunkPositions().forEach(chunkPos -> {
            Set<T> claims = this.byChunkPosMap.get(chunkPos);
            if (claims != null) claims.remove(claim);
        });

        Set<T> claims = this.byOwnerIdMap.get(claim.getOwnerId());
        if (claims != null) claims.remove(claim);
    }

    public void clear() {
        this.byIdMap.clear();
        this.byChunkPosMap.clear();
        this.byOwnerIdMap.clear();
    }

    @NotNull
    public Map<String, T> getByIdMap() {
        return this.byIdMap;
    }

    @NotNull
    public Map<ChunkPos, Set<T>> getByChunkPosMap() {
        return this.byChunkPosMap;
    }

    @NotNull
    public Map<UUID, Set<T>> getByOwnerIdMap() {
        return this.byOwnerIdMap;
    }

    @NotNull
    public Set<String> getClaimIds() {
        return new HashSet<>(this.byIdMap.keySet());
    }

    @Nullable
    public T getClaimById(@NotNull String id) {
        return this.byIdMap.get(id.toLowerCase());
    }

    @NotNull
    public Set<T> getClaims() {
        return new HashSet<>(this.byIdMap.values());
    }

    // Get claims by ChunkPos.

    @NotNull
    public Set<T> getClaimsInChunk(@NotNull Chunk chunk) {
        return this.getClaimsInChunk(ChunkPos.from(chunk));
    }

    @NotNull
    public Set<T> getClaimsInChunk(int chunkX, int chunkZ) {
        return this.getClaimsInChunk(new ChunkPos(chunkX, chunkZ));
    }

    @NotNull
    public Set<T> getClaimsInChunk(@NotNull ChunkPos chunkPos) {
        return this.byChunkPosMap.getOrDefault(chunkPos, Collections.emptySet());
    }

    // Get claims by Owner ID.

    @NotNull
    public Set<T> getClaimsByOwner(@NotNull UUID ownerId) {
        return this.byOwnerIdMap.getOrDefault(ownerId, Collections.emptySet());
    }

    // Get claims in Cuboid.

    @NotNull
    public Set<T> getClaimsInCuboid(@NotNull Cuboid cuboid) {
        return cuboid.getIntersectingChunkPositions().stream()
            .flatMap(chunkPos -> this.getClaimsInChunk(chunkPos).stream()) // Get all claims of a cuboid chunk.
            .filter(claim -> claim.isIntersecting(cuboid)) // Ensure RegionClaims are actually intersects with the cuboid.
            .collect(Collectors.toSet());
    }



    // Get claims by Location.

    @NotNull
    public Set<T> getClaimsAt(@NotNull Location location) {
        return this.getClaimsAt(BlockPos.from(location));
    }

    @NotNull
    public Set<T> getClaimsAt(@NotNull BlockPos blockPos) {
        return this.getClaimsInChunk(ChunkPos.from(blockPos)).stream() // Get all claims of a chunk.
            .filter(claim -> claim.isInside(blockPos)) // Ensure RegionClaims are actually contains the BlockPos.
            .collect(Collectors.toSet());
    }
}
