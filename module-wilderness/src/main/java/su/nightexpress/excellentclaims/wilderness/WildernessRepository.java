package su.nightexpress.excellentclaims.wilderness;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class WildernessRepository implements ClaimRepository<WildernessRegion> {

    private final Map<AdaptedKey, WildernessRegion> regionsByWorld;
    private final Map<Identifier, WildernessRegion> regionsById;

    public WildernessRepository() {
        this.regionsByWorld = new HashMap<>();
        this.regionsById = new HashMap<>();
    }

    @Override
    public void clear() {
        this.regionsByWorld.clear();
        this.regionsById.clear();
    }

    @Override
    public void clear(World world) {
        this.clear(BukkitKeys.getKey(world));
    }

    @Override
    public void clear(AdaptedKey worldKey) {
        this.regionsByWorld.remove(worldKey);
    }

    @Override
    public int countClaims(Player player) {
        return 0;
    }

    @Override
    public int countClaims(UUID playerId) {
        return 0;
    }

    @Override
    public @Nullable WildernessRegion get(Identifier key) {
        return this.regionsById.get(key);
    }

    public @Nullable WildernessRegion getInWorld(World world) {
        return this.getInWorld(BukkitKeys.getKey(world));
    }

    public @Nullable WildernessRegion getInWorld(AdaptedKey worldKey) {
        return this.regionsByWorld.get(worldKey);
    }

    @Override
    public Set<WildernessRegion> getAt(Location location) {
        World world = location.getWorld();
        return world == null ? Set.of() : Collections.singleton(this.getInWorld(world));
    }

    @Override
    public Set<WildernessRegion> getAt(Block block) {
        return Collections.singleton(this.getInWorld(block.getWorld()));
    }

    @Override
    public Set<WildernessRegion> getAt(World world, BlockPos blockPos) {
        return Collections.singleton(this.getInWorld(world));
    }

    @Override
    public Set<WildernessRegion> getAt(AdaptedKey worldId, BlockPos blockPos) {
        return Collections.singleton(this.getInWorld(worldId));
    }

    @Override
    public Set<WildernessRegion> getByOwner(OfflinePlayer player) {
        return Set.of();
    }

    @Override
    public Set<WildernessRegion> getByOwner(UUID ownerId) {
        return Set.of();
    }

    @Override
    public Set<WildernessRegion> getInChunk(Chunk chunk) {
        return Collections.singleton(this.getInWorld(chunk.getWorld()));
    }

    @Override
    public Set<WildernessRegion> getInChunk(World world, ChunkPos chunkPos) {
        return Collections.singleton(this.getInWorld(world));
    }

    @Override
    public Set<WildernessRegion> getInChunk(World world, int chunkX, int chunkZ) {
        return Collections.singleton(this.getInWorld(world));
    }

    @Override
    public Set<WildernessRegion> getInChunk(AdaptedKey worldId, ChunkPos chunkPos) {
        return Collections.singleton(this.getInWorld(worldId));
    }

    @Override
    public Set<WildernessRegion> getInChunk(AdaptedKey worldId, int chunkX, int chunkZ) {
        return Collections.singleton(this.getInWorld(worldId));
    }

    @Override
    public Set<WildernessRegion> getInCuboid(World world, Cuboid cuboid) {
        return Collections.singleton(this.getInWorld(world));
    }

    @Override
    public Set<WildernessRegion> getInCuboid(AdaptedKey worldId, Cuboid cuboid) {
        return Collections.singleton(this.getInWorld(worldId));
    }

    @Override
    public @Nullable WildernessRegion getPrioritizedClaim(Block block) {
        return this.getInWorld(block.getWorld());
    }

    @Override
    public @Nullable WildernessRegion getPrioritizedClaim(Location location) {
        World world = location.getWorld();
        return world == null ? null : this.getInWorld(world);
    }

    @Override
    public @Nullable WildernessRegion getPrioritizedClaim(World world, BlockPos blockPos) {
        return this.getInWorld(world);
    }

    @Override
    public @Nullable WildernessRegion getPrioritizedClaim(AdaptedKey worldId, BlockPos blockPos) {
        return this.getInWorld(worldId);
    }

    @Override
    public Set<String> idValues() {
        return this.regionsById.keySet().stream().map(Identifier::value).collect(Collectors.toSet());
    }

    @Override
    public Set<String> idValues(World world) {
        return this.idValues(BukkitKeys.getKey(world));
    }

    @Override
    public Set<String> idValues(AdaptedKey worldKey) {
        WildernessRegion region = this.getInWorld(worldKey);
        return region == null ? Set.of() : Collections.singleton(region.id().value());
    }

    @Override
    public Set<Identifier> ids() {
        return Set.copyOf(this.regionsById.keySet());
    }

    @Override
    public Set<Identifier> ids(World world) {
        return this.ids(BukkitKeys.getKey(world));
    }

    @Override
    public Set<Identifier> ids(AdaptedKey worldKey) {
        WildernessRegion region = this.getInWorld(worldKey);
        return region == null ? Set.of() : Collections.singleton(region.id());
    }

    @Override
    public boolean isClaimed(Location location) {
        return this.getPrioritizedClaim(location) != null;
    }

    @Override
    public boolean isClaimed(Block block) {
        return this.getPrioritizedClaim(block) != null;
    }

    @Override
    public boolean isClaimed(World world, BlockPos blockPos) {
        return this.getInWorld(world) != null;
    }

    @Override
    public boolean isClaimed(AdaptedKey worldId, BlockPos blockPos) {
        return this.getInWorld(worldId) != null;
    }

    @Override
    public boolean isEmpty() {
        return this.regionsByWorld.isEmpty();
    }

    @Override
    public boolean isEmpty(World world) {
        return this.isEmpty(BukkitKeys.getKey(world));
    }

    @Override
    public boolean isEmpty(AdaptedKey worldKey) {
        return this.getInWorld(worldKey) == null;
    }

    @Override
    public Optional<WildernessRegion> lookup(Identifier key) {
        return Optional.ofNullable(this.regionsById.get(key));
    }

    @Override
    public void register(WildernessRegion region) {
        this.regionsById.put(region.id(), region);
        this.regionsByWorld.put(region.getWorldKey(), region);
    }

    @Override
    public void reindex(WildernessRegion region) {
        this.remove(region);
        this.register(region);
    }

    @Override
    public @Nullable WildernessRegion remove(WildernessRegion region) {
        WildernessRegion removed = this.regionsById.remove(region.id());
        this.regionsByWorld.remove(region.getWorldKey());

        return removed;
    }

    @Override
    public @Nullable WildernessRegion remove(Identifier id) {
        return this.regionsById.remove(id);
    }

    @Override
    public int size() {
        return this.regionsById.size();
    }

    @Override
    public int size(World world) {
        return this.size(BukkitKeys.getKey(world));
    }

    @Override
    public int size(AdaptedKey worldKey) {
        return this.getInWorld(worldKey) != null ? 1 : 0;
    }

    @Override
    public Stream<WildernessRegion> stream() {
        return this.regionsByWorld.values().stream();
    }

    @Override
    public Stream<WildernessRegion> stream(World world) {
        return this.stream(BukkitKeys.getKey(world));
    }

    @Override
    public Stream<WildernessRegion> stream(AdaptedKey worldKey) {
        return Optional.of(this.regionsByWorld.get(worldKey)).stream();
    }

    @Override
    public Set<WildernessRegion> values() {
        return Set.copyOf(this.regionsByWorld.values());
    }

    @Override
    public Set<WildernessRegion> values(World world) {
        return this.values(BukkitKeys.getKey(world));
    }

    @Override
    public Set<WildernessRegion> values(AdaptedKey worldKey) {
        WildernessRegion region = this.getInWorld(worldKey);
        return region == null ? Set.of() : Collections.singleton(region);
    }
}
