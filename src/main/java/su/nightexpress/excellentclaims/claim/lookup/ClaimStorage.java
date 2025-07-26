package su.nightexpress.excellentclaims.claim.lookup;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.claim.LandClaim;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.claim.impl.Wilderness;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClaimStorage {

    private final ClaimLookup<RegionClaim> regionLookup;
    private final ClaimLookup<LandClaim> landLookup;
    private final Map<String, Wilderness> wildernessMap;

    public ClaimStorage() {
        this.regionLookup = new ClaimLookup<>();
        this.landLookup = new ClaimLookup<>();
        this.wildernessMap = new HashMap<>();
    }

    public void update(@NotNull Claim claim) {
        this.remove(claim);
        this.add(claim);
    }

    public void add(@NotNull Claim claim) {
        if (claim instanceof Wilderness wilderness) {
            this.wildernessMap.put(claim.getWorldName().toLowerCase(), wilderness);
        }
        else if (claim instanceof LandClaim landClaim) {
            this.landLookup.update(landClaim);
        }
        else if (claim instanceof RegionClaim regionClaim) {
            this.regionLookup.update(regionClaim);
        }
    }

    public void remove(@NotNull Claim claim) {
        if (claim instanceof Wilderness) {
            this.wildernessMap.remove(claim.getWorldName().toLowerCase());
        }
        else if (claim instanceof LandClaim landClaim) {
            this.landLookup.purge(landClaim);
        }
        else if (claim instanceof RegionClaim regionClaim) {
            this.regionLookup.purge(regionClaim);
        }
    }

    public void clear() {
        this.regionLookup.clear();
        this.landLookup.clear();
        this.wildernessMap.clear();
    }

    @NotNull
    public Map<String, Wilderness> getWildernessMap() {
        return this.wildernessMap;
    }

    @NotNull
    public ClaimLookup<RegionClaim> regionLookup() {
        return this.regionLookup;
    }

    @NotNull
    public ClaimLookup<LandClaim> landLookup() {
        return this.landLookup;
    }

    @NotNull
    public Set<Claim> getClaimsAndWildernesses() {
        Set<Claim> claims = new HashSet<>();
        claims.addAll(this.getClaims());
        claims.addAll(this.getWildernesses());
        return claims;
    }

    // Get all (in)active claims.

    @NotNull
    public Set<Claim> getActiveClaims() {
        return this.getClaims().stream().filter(Claim::isActive).collect(Collectors.toSet());
    }

    @NotNull
    public Set<Claim> getInactiveClaims() {
        return this.getClaims().stream().filter(Predicate.not(Claim::isActive)).collect(Collectors.toSet());
    }

    // Get Wilderness

    @NotNull
    public Set<Wilderness> getWildernesses() {
        return new HashSet<>(this.wildernessMap.values());
    }

    @Nullable
    public Wilderness getWilderness(@NotNull World world) {
        return this.getWilderness(world.getName());
    }

    @Nullable
    public Wilderness getWilderness(@NotNull String worldName) {
        return this.wildernessMap.get(worldName.toLowerCase());
    }

    // Get all claims.

    @NotNull
    public ClaimLookup<? extends Claim> lookup(@NotNull ClaimType type) {
        return type == ClaimType.CHUNK ? this.landLookup : this.regionLookup;
    }

    @NotNull
    private Set<ClaimLookup<? extends Claim>> lookups() {
        return Stream.of(ClaimType.values()).map(this::lookup).collect(Collectors.toSet());
    }

    @NotNull
    public Set<Claim> getClaims() {
        return this.lookups().stream().flatMap(lookup -> lookup.getAll().stream()).collect(Collectors.toSet());
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull ClaimType type) {
        return new HashSet<>(this.lookup(type).getAll());
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull ClaimType type, @NotNull Predicate<Claim> predicate) {
        return this.getClaims(type).stream().filter(predicate).collect(Collectors.toCollection(HashSet::new));
    }

    // Get all claims in a World.

    @NotNull
    public Set<Claim> getClaims(@NotNull World world) {
        return this.getClaims(world.getName());
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull String worldName) {
        return this.lookups().stream().flatMap(lookup -> lookup.getAllInWorld(worldName).stream()).collect(Collectors.toSet());
    }

    // Get all claims of ClaimType in a World.

    @NotNull
    public Set<Claim> getClaims(@NotNull World world, @NotNull ClaimType type) {
        return this.getClaims(world.getName(), type);
    }

    @NotNull
    public Set<Claim> getClaims(@NotNull String worldName, @NotNull ClaimType type) {
        return new HashSet<>(this.lookup(type).getAllInWorld(worldName));
    }

    // Get all claims in a World intersecting with a Cuboid.

    @NotNull
    public Set<Claim> getClaimsInCuboid(@NotNull World world, @NotNull Cuboid cuboid) {
        return this.getClaimsInCuboid(world.getName(), cuboid);
    }

    @NotNull
    public Set<Claim> getClaimsInCuboid(@NotNull String worldName, @NotNull Cuboid cuboid) {
        return this.lookups().stream().flatMap(lookup -> lookup.getInCuboid(worldName, cuboid).stream()).collect(Collectors.toSet());
    }

    // Get claims by owner UUID.

    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull OfflinePlayer player) {
        return this.getClaimsByOwner(player.getUniqueId());
    }

    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull UUID playerId) {
        return this.lookups().stream().flatMap(lookup -> lookup.getByOwner(playerId).stream()).collect(Collectors.toSet());
    }


    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull OfflinePlayer player, @NotNull ClaimType type) {
        return this.getClaimsByOwner(player.getUniqueId(), type);
    }

    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull UUID playerId, @NotNull ClaimType type) {
        return new HashSet<>(this.lookup(type).getByOwner(playerId));
    }


    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull OfflinePlayer player, @NotNull World world) {
        return this.getClaimsByOwner(player.getUniqueId(), world);
    }

    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull UUID playerId, @NotNull World world) {
        return this.getClaimsByOwner(playerId, world.getName());
    }

    @NotNull
    public Set<Claim> getClaimsByOwner(@NotNull UUID playerId, @NotNull String worldName) {
        return this.lookups().stream().flatMap(lookup -> lookup.getByOwner(playerId, worldName).stream()).collect(Collectors.toSet());
    }

    // Get claims by locations.

    @NotNull
    public Set<Claim> getClaimsAt(@NotNull Location location) {
        return Optional.ofNullable(location.getWorld()).map(world -> this.getClaimsAt(world, BlockPos.from(location))).orElse(Collections.emptySet());
    }

    @NotNull
    public Set<Claim> getClaimsAt(@NotNull World world, @NotNull ChunkPos chunkPos) {
        return this.getClaimsAt(world.getName(), chunkPos);
    }

    @NotNull
    public Set<Claim> getClaimsAt(@NotNull String worldName, @NotNull ChunkPos chunkPos) {
        return this.lookups().stream().flatMap(lookup -> lookup.getAt(worldName, chunkPos).stream()).collect(Collectors.toSet());
    }

    @NotNull
    public Set<Claim> getClaimsAt(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.getClaimsAt(world.getName(), blockPos);
    }

    @NotNull
    public Set<Claim> getClaimsAt(@NotNull String worldName, @NotNull BlockPos blockPos) {
        return this.lookups().stream().flatMap(lookup -> lookup.getAt(worldName, blockPos).stream()).collect(Collectors.toSet());
    }
}
