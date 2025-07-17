package su.nightexpress.excellentclaims.claim.lookup;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClaimLookup<T extends Claim> {

    private final Map<String, WorldLookup<T>> lookupByWorld;

    public ClaimLookup() {
        this.lookupByWorld = new HashMap<>();
    }

    public void update(@NotNull T claim) {
        this.purge(claim);
        this.store(claim);
    }

    public void store(@NotNull T claim) {
        this.worldLookup(claim.getWorldName()).store(claim);
    }

    public void purge(@NotNull T claim) {
        this.worldLookup(claim.getWorldName()).purge(claim);
    }

    public void clear() {
        this.lookupByWorld.values().forEach(WorldLookup::clear);
        this.lookupByWorld.clear();
    }

    @NotNull
    public Map<String, WorldLookup<T>> getLookupByWorld() {
        return this.lookupByWorld;
    }

    @NotNull
    public WorldLookup<T> worldLookup(@NotNull World world) {
        return this.worldLookup(world.getName());
    }

    @NotNull
    public Set<WorldLookup<T>> worldLookups() {
        return new HashSet<>(this.lookupByWorld.values());
    }

    @NotNull
    public WorldLookup<T> worldLookup(@NotNull String worldName) {
        return this.lookupByWorld.computeIfAbsent(worldName.toLowerCase(), k -> new WorldLookup<>());
    }

    // Get all claims in all worlds.

    @NotNull
    public Set<T> getAll() {
        return this.worldLookups().stream().flatMap(lookup -> lookup.getClaims().stream()).collect(Collectors.toSet());
    }

    // Get claims by World.

    @NotNull
    public Set<T> getAllInWorld(@NotNull World world) {
        return this.getAllInWorld(world.getName());
    }

    @NotNull
    public Set<T> getAllInWorld(@NotNull String worldName) {
        return this.worldLookup(worldName).getClaims();
    }

    // Get claims by owner UUID.

    @NotNull
    public Set<T> getByOwner(@NotNull OfflinePlayer player) {
        return this.getByOwner(player.getUniqueId());
    }

    @NotNull
    public Set<T> getByOwner(@NotNull UUID ownerId) {
        return this.worldLookups().stream().flatMap(lookup -> lookup.getClaimsByOwner(ownerId).stream()).collect(Collectors.toSet());
    }

    @NotNull
    public Set<T> getByOwner(@NotNull UUID playerId, @NotNull World world) {
        return this.getByOwner(playerId, world.getName());
    }

    @NotNull
    public Set<T> getByOwner(@NotNull UUID playerId, @NotNull String worldName) {
        return this.worldLookup(worldName).getClaimsByOwner(playerId);
    }

    // Get claims in Cuboid.

    @NotNull
    public Set<T> getInCuboid(@NotNull World world, @NotNull Cuboid cuboid) {
        return this.getInCuboid(world.getName(), cuboid);
    }

    @NotNull
    public Set<T> getInCuboid(@NotNull String worldName, @NotNull Cuboid cuboid) {
        return this.worldLookup(worldName).getClaimsInCuboid(cuboid);
    }

    // Get claims by Location.

    @NotNull
    public Set<T> getAt(@NotNull Location location) {
        return Optional.ofNullable(location.getWorld()).map(world -> this.worldLookup(world).getClaimsAt(location)).orElse(Collections.emptySet());
    }

    // Get claims by BlockPos in a World.

    @NotNull
    public Set<T> getAt(@NotNull World world, @NotNull BlockPos blockPos) {
        return this.worldLookup(world).getClaimsAt(blockPos);
    }

    @NotNull
    public Set<T> getAt(@NotNull String worldName, @NotNull BlockPos blockPos) {
        return this.worldLookup(worldName).getClaimsAt(blockPos);
    }

    // Get claims by ChunkPos in a World.

    @NotNull
    public Set<T> getAt(@NotNull Chunk chunk) {
        return this.worldLookup(chunk.getWorld()).getClaimsInChunk(chunk);
    }

    @NotNull
    public Set<T> getAt(@NotNull World world, int chunkX, int chunkZ) {
        return this.worldLookup(world).getClaimsInChunk(chunkX, chunkZ);
    }

    @NotNull
    public Set<T> getAt(@NotNull World world, @NotNull ChunkPos chunkPos) {
        return this.worldLookup(world).getClaimsInChunk(chunkPos);
    }

    @NotNull
    public Set<T> getAt(@NotNull String worldName, @NotNull ChunkPos chunkPos) {
        return this.worldLookup(worldName).getClaimsInChunk(chunkPos);
    }

    // Get exact claim by ID.

    @Nullable
    public T getById(@NotNull World world, @NotNull String id) {
        return this.getById(world.getName(), id);
    }

    @Nullable
    public T getById(@NotNull String worldName, @NotNull String id) {
        return this.worldLookup(worldName).getClaimById(id);
    }

    // Get land names.

    @NotNull
    public List<String> getNames(@NotNull World world) {
        return this.getNames(world.getName());
    }

    @NotNull
    public List<String> getNames(@NotNull String worldName) {
        return this.getNames(worldName, claim -> true);
    }

    @NotNull
    public List<String> getNames(@NotNull Player player, @NotNull ClaimPermission permission) {
        return this.getNames(player, claim -> claim.hasPermission(player, permission));
    }

    @NotNull
    public List<String> getNames(@NotNull Player player, @NotNull Predicate<Claim> predicate) {
        return this.getNames(player.getWorld(), predicate);
    }

    @NotNull
    public List<String> getNames(@NotNull World world, @NotNull Predicate<Claim> predicate) {
        return this.getNames(world.getName(), predicate);
    }

    @NotNull
    public List<String> getNames(@NotNull String worldName, @NotNull Predicate<Claim> predicate) {
        return this.getAllInWorld(worldName).stream().filter(predicate).map(Claim::getId).toList();
    }
}
