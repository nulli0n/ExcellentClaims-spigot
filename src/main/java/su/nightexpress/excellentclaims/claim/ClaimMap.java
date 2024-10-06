package su.nightexpress.excellentclaims.claim;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;

import java.util.*;

public class ClaimMap {

    // Map Claims by World -> ID/ChunkPos
    private final Map<String, Map<String, RegionClaim>> regionClaimMap;
    private final Map<String, Map<ChunkPos, ChunkClaim>> chunkClaimMap;

    // Map Regions by World -> ChunkPos
    private final Map<String, Map<ChunkPos, Set<RegionClaim>>> regionsByChunkMap;

    // Map Claims by Owner UUID -> World
    private final Map<UUID, Map<String, Set<RegionClaim>>> playerRegionMap;
    private final Map<UUID, Map<String, Set<ChunkClaim>>> playerChunkMap;

    public ClaimMap() {
        this.regionClaimMap = new HashMap<>();
        this.chunkClaimMap = new HashMap<>();

        this.regionsByChunkMap = new HashMap<>();

        this.playerRegionMap = new HashMap<>();
        this.playerChunkMap = new HashMap<>();
    }

    public void update(@NotNull Claim claim) {
        this.remove(claim);
        this.add(claim);
    }

    public void add(@NotNull Claim claim) {
        String worldName = claim.getWorldName();

        if (claim instanceof ChunkClaim chunkClaim) {
            chunkClaim.getPositions().forEach(chunkPos -> {
                this.chunkClaimMap.computeIfAbsent(worldName, k -> new HashMap<>()).put(chunkPos, chunkClaim);
            });
            //this.chunkClaimMap.computeIfAbsent(worldName, k -> new HashMap<>()).put(chunkClaim.getPosition(), chunkClaim);

            this.playerChunkMap.computeIfAbsent(claim.getOwnerId(), k -> new HashMap<>()).computeIfAbsent(worldName,  k -> new HashSet<>()).add(chunkClaim);
        }
        else if (claim instanceof RegionClaim regionClaim) {
            this.regionClaimMap.computeIfAbsent(worldName, k -> new HashMap<>()).put(regionClaim.getId(), regionClaim);

            Map<ChunkPos, Set<RegionClaim>> byChunkMap = this.regionsByChunkMap.computeIfAbsent(worldName, k -> new HashMap<>());
            regionClaim.getCuboid().getIntersectingChunkPositions().forEach(chunkPos -> {
                byChunkMap.computeIfAbsent(chunkPos, k -> new HashSet<>()).add(regionClaim);
            });

            this.playerRegionMap.computeIfAbsent(claim.getOwnerId(), k -> new HashMap<>()).computeIfAbsent(worldName, k -> new HashSet<>()).add(regionClaim);
        }
    }

    public void remove(@NotNull Claim claim) {
        String worldName = claim.getWorldName();

        if (claim instanceof ChunkClaim chunkClaim) {
            chunkClaim.getPositions().forEach(chunkPos -> {
                this.getChunkClaimMap(worldName).remove(chunkPos);
            });
            //this.getChunkClaimMap(worldName).remove(chunkClaim.getPosition());

            this.getPlayerChunks(claim.getOwnerId(), worldName).remove(chunkClaim);
        }
        else if (claim instanceof RegionClaim regionClaim) {
            this.getRegionClaimMap(worldName).remove(regionClaim.getId());

            Map<ChunkPos, Set<RegionClaim>> byChunkMap = this.regionsByChunkMap.getOrDefault(worldName, Collections.emptyMap());
            //regionClaim.getCuboid().getIntersectingChunkPositions().forEach(byChunkMap::remove);
            regionClaim.getCuboid().getIntersectingChunkPositions().forEach(chunkPos -> {
                var set = byChunkMap.get(chunkPos);
                if (set != null) set.remove(regionClaim);
            });

            //byChunkMap.values().forEach(regions -> regions.remove(regionClaim));
            byChunkMap.values().removeIf(Set::isEmpty);

            this.getPlayerRegions(claim.getOwnerId(), worldName).remove(regionClaim);
        }
    }

    public void clear() {
        this.chunkClaimMap.clear();
        this.regionClaimMap.clear();
        this.regionsByChunkMap.clear();
        this.playerChunkMap.clear();
        this.playerRegionMap.clear();
    }

    @NotNull
    public Map<String, Map<String, RegionClaim>> getRegionClaimMap() {
        return this.regionClaimMap;
    }

    @NotNull
    public Map<String, Map<ChunkPos, ChunkClaim>> getChunkClaimMap() {
        return this.chunkClaimMap;
    }


    @NotNull
    public Map<String, RegionClaim> getRegionClaimMap(@NotNull World world) {
        return this.getRegionClaimMap(world.getName());
    }

    @NotNull
    public Map<ChunkPos, ChunkClaim> getChunkClaimMap(@NotNull World world) {
        return this.getChunkClaimMap(world.getName());
    }


    @NotNull
    public Map<String, RegionClaim> getRegionClaimMap(@NotNull String worldName) {
        return this.regionClaimMap.getOrDefault(worldName.toLowerCase(), Collections.emptyMap());
    }

    @NotNull
    public Map<ChunkPos, ChunkClaim> getChunkClaimMap(@NotNull String worldName) {
        return this.chunkClaimMap.getOrDefault(worldName.toLowerCase(), Collections.emptyMap());
    }



    @NotNull
    public Map<ChunkPos, Set<RegionClaim>> getRegionsByChunkMap(@NotNull World world) {
        return this.getRegionsByChunkMap(world.getName());
    }

    @NotNull
    public Map<ChunkPos, Set<RegionClaim>> getRegionsByChunkMap(@NotNull String worldName) {
        return this.regionsByChunkMap.getOrDefault(worldName.toLowerCase(), Collections.emptyMap());
    }

    @NotNull
    public Set<RegionClaim> getRegionsByChunk(@NotNull World world, @NotNull ChunkPos chunkPos) {
        return this.getRegionsByChunk(world.getName(), chunkPos);
    }

    @NotNull
    public Set<RegionClaim> getRegionsByChunk(@NotNull String worldName, @NotNull ChunkPos chunkPos) {
        return this.getRegionsByChunkMap(worldName.toLowerCase()).getOrDefault(chunkPos, Collections.emptySet());
    }



    @NotNull
    public Map<UUID, Map<String, Set<ChunkClaim>>> getPlayerChunkMap() {
        return this.playerChunkMap;
    }

    @NotNull
    public Map<UUID, Map<String, Set<RegionClaim>>> getPlayerRegionMap() {
        return this.playerRegionMap;
    }

    @NotNull
    public Map<String, Set<ChunkClaim>> getPlayerChunks(@NotNull UUID playerId) {
        return this.playerChunkMap.getOrDefault(playerId, Collections.emptyMap());
    }

    @NotNull
    public Map<String, Set<RegionClaim>> getPlayerRegions(@NotNull UUID playerId) {
        return this.playerRegionMap.getOrDefault(playerId, Collections.emptyMap());
    }

    @NotNull
    public Set<ChunkClaim> getPlayerChunks(@NotNull UUID playerId, @NotNull String worldName) {
        return this.getPlayerChunks(playerId).getOrDefault(worldName.toLowerCase(), Collections.emptySet());
    }

    @NotNull
    public Set<RegionClaim> getPlayerRegions(@NotNull UUID playerId, @NotNull String worldName) {
        return this.getPlayerRegions(playerId).getOrDefault(worldName.toLowerCase(), Collections.emptySet());
    }
}
