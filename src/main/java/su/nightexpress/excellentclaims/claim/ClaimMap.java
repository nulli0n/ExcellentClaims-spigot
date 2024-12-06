package su.nightexpress.excellentclaims.claim;

import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.LandClaim;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.claim.impl.Wilderness;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;

import java.util.*;

public class ClaimMap {

    // Map Claims by World -> ID/ChunkPos
    private final Map<String, Map<String, RegionClaim>> regionByIdMap;
    private final Map<String, Map<String, LandClaim>>   landByIdMap;

    // Map Regions by World -> ChunkPos
    private final Map<String, Map<ChunkPos, Set<RegionClaim>>> regionsByChunkMap;
    private final Map<String, Map<ChunkPos, LandClaim>>        landsByChunkMap;

    // Map Claims by Owner UUID -> World
    private final Map<UUID, Map<String, Set<RegionClaim>>> playerRegionMap;
    private final Map<UUID, Map<String, Set<LandClaim>>>   playerChunkMap;

    private final Map<String, Wilderness> wildernessMap;

    public ClaimMap() {
        this.regionByIdMap = new HashMap<>();
        this.landByIdMap = new HashMap<>();

        this.regionsByChunkMap = new HashMap<>();
        this.landsByChunkMap = new HashMap<>();

        this.playerRegionMap = new HashMap<>();
        this.playerChunkMap = new HashMap<>();

        this.wildernessMap = new HashMap<>();
    }

    public void update(@NotNull Claim claim) {
        this.remove(claim);
        this.add(claim);
    }

    public void add(@NotNull Claim claim) {
        String worldName = claim.getWorldName().toLowerCase();

        if (claim instanceof Wilderness wilderness) {
            this.wildernessMap.put(worldName, wilderness);
        }
        else if (claim instanceof LandClaim landClaim) {
            landClaim.getPositions().forEach(chunkPos -> {
                this.landByIdMap.computeIfAbsent(worldName, k -> new HashMap<>()).put(landClaim.getId(), landClaim);
                this.landsByChunkMap.computeIfAbsent(worldName, k -> new HashMap<>()).put(chunkPos, landClaim);
            });

            this.playerChunkMap.computeIfAbsent(claim.getOwnerId(), k -> new HashMap<>()).computeIfAbsent(worldName,  k -> new HashSet<>()).add(landClaim);
        }
        else if (claim instanceof RegionClaim regionClaim) {
            this.regionByIdMap.computeIfAbsent(worldName, k -> new HashMap<>()).put(regionClaim.getId(), regionClaim);

            Map<ChunkPos, Set<RegionClaim>> byChunkMap = this.regionsByChunkMap.computeIfAbsent(worldName, k -> new HashMap<>());
            regionClaim.getCuboid().getIntersectingChunkPositions().forEach(chunkPos -> {
                byChunkMap.computeIfAbsent(chunkPos, k -> new HashSet<>()).add(regionClaim);
            });

            this.playerRegionMap.computeIfAbsent(claim.getOwnerId(), k -> new HashMap<>()).computeIfAbsent(worldName, k -> new HashSet<>()).add(regionClaim);
        }
    }

    public void remove(@NotNull Claim claim) {
        String worldName = claim.getWorldName().toLowerCase();

        if (claim instanceof Wilderness wilderness) {
            this.wildernessMap.remove(worldName);
        }
        else if (claim instanceof LandClaim landClaim) {
            this.getLandByIdMap(worldName).remove(landClaim.getId());

            var byChunkMap = this.landsByChunkMap.getOrDefault(worldName, Collections.emptyMap());
            landClaim.getPositions().forEach(byChunkMap::remove);

            this.getPlayerLands(claim.getOwnerId(), worldName).remove(landClaim);
        }
        else if (claim instanceof RegionClaim regionClaim) {
            this.getRegionByIdMap(worldName).remove(regionClaim.getId());

            Map<ChunkPos, Set<RegionClaim>> byChunkMap = this.regionsByChunkMap.getOrDefault(worldName, Collections.emptyMap());
            regionClaim.getCuboid().getIntersectingChunkPositions().forEach(chunkPos -> {
                var set = byChunkMap.get(chunkPos);
                if (set != null) set.remove(regionClaim);
            });

            byChunkMap.values().removeIf(Set::isEmpty);

            this.getPlayerRegions(claim.getOwnerId(), worldName).remove(regionClaim);
        }
    }

    public void clear() {
        this.landByIdMap.clear();
        this.regionByIdMap.clear();
        this.regionsByChunkMap.clear();
        this.landsByChunkMap.clear();
        this.playerChunkMap.clear();
        this.playerRegionMap.clear();
        this.wildernessMap.clear();
    }

    @NotNull
    public Map<String, Wilderness> getWildernessMap() {
        return this.wildernessMap;
    }

    @Nullable
    public Wilderness getWilderness(@NotNull World world) {
        return this.getWilderness(world.getName());
    }

    @Nullable
    public Wilderness getWilderness(@NotNull String worldName) {
        return this.wildernessMap.get(worldName.toLowerCase());
    }

    @NotNull
    public Map<String, Map<String, RegionClaim>> getRegionByIdMap() {
        return this.regionByIdMap;
    }

    @NotNull
    public Map<String, Map<String, LandClaim>> getLandByIdMap() {
        return this.landByIdMap;
    }


    @NotNull
    public Map<String, RegionClaim> getRegionByIdMap(@NotNull World world) {
        return this.getRegionByIdMap(world.getName());
    }

    @NotNull
    public Map<String, LandClaim> getLandByIdMap(@NotNull World world) {
        return this.getLandByIdMap(world.getName());
    }


    @NotNull
    public Map<String, RegionClaim> getRegionByIdMap(@NotNull String worldName) {
        return this.regionByIdMap.getOrDefault(worldName.toLowerCase(), Collections.emptyMap());
    }

    @NotNull
    public Map<String, LandClaim> getLandByIdMap(@NotNull String worldName) {
        return this.landByIdMap.getOrDefault(worldName.toLowerCase(), Collections.emptyMap());
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
    public Map<ChunkPos, LandClaim> getLandByChunkMap(@NotNull World world) {
        return this.getLandByChunkMap(world.getName());
    }

    @NotNull
    public Map<ChunkPos, LandClaim> getLandByChunkMap(@NotNull String worldName) {
        return this.landsByChunkMap.getOrDefault(worldName.toLowerCase(), Collections.emptyMap());
    }



    @NotNull
    public Map<UUID, Map<String, Set<LandClaim>>> getPlayerLandMap() {
        return this.playerChunkMap;
    }

    @NotNull
    public Map<UUID, Map<String, Set<RegionClaim>>> getPlayerRegionMap() {
        return this.playerRegionMap;
    }

    @NotNull
    public Map<String, Set<LandClaim>> getPlayerLands(@NotNull UUID playerId) {
        return this.playerChunkMap.getOrDefault(playerId, Collections.emptyMap());
    }

    @NotNull
    public Map<String, Set<RegionClaim>> getPlayerRegions(@NotNull UUID playerId) {
        return this.playerRegionMap.getOrDefault(playerId, Collections.emptyMap());
    }

    @NotNull
    public Set<LandClaim> getPlayerLands(@NotNull UUID playerId, @NotNull String worldName) {
        return this.getPlayerLands(playerId).getOrDefault(worldName.toLowerCase(), Collections.emptySet());
    }

    @NotNull
    public Set<RegionClaim> getPlayerRegions(@NotNull UUID playerId, @NotNull String worldName) {
        return this.getPlayerRegions(playerId).getOrDefault(worldName.toLowerCase(), Collections.emptySet());
    }
}
