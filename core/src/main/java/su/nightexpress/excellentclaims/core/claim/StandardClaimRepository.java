package su.nightexpress.excellentclaims.core.claim;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.core.id.Identifiable;
import su.nightexpress.excellentclaims.api.core.id.IdentifiableRegistry;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.util.ChunkUtil;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class StandardClaimRepository<T extends OwnableClaim> extends IdentifiableRegistry<T> implements ClaimRepository<T> {

    private final Map<AdaptedKey, Long2ObjectMap<Set<T>>> worldIndexes;
    private final Map<UUID, Set<T>>                       byOwnerIdMap;

    public StandardClaimRepository() {
        super();
        this.worldIndexes = new HashMap<>();
        this.byOwnerIdMap = new HashMap<>();
    }

    @Override
    public void clear() {
        super.clear();
        this.worldIndexes.clear();
        this.byOwnerIdMap.clear();
    }

    @Override
    public void clear(World world) {
        this.clear(BukkitKeys.getKey(world));
    }

    @Override
    public void clear(AdaptedKey worldKey) {
        this.worldIndexes.remove(worldKey);
    }

    @Override
    public int countClaims(Player player) {
        return this.countClaims(player.getUniqueId());
    }

    @Override
    public int countClaims(UUID playerId) {
        return this.byOwnerIdMap.getOrDefault(playerId, Set.of()).size();
    }

    @Override
    public Set<T> getAt(Location location) {
        return this.getAt(location.getWorld(), BlockPos.from(location));
    }

    @Override
    public Set<T> getAt(Block block) {
        return this.getAt(block.getWorld(), BlockPos.from(block));
    }

    @Override
    public Set<T> getAt(World world, BlockPos blockPos) {
        return this.getAt(BukkitKeys.getKey(world), blockPos);
    }

    @Override
    public Set<T> getAt(AdaptedKey worldId, BlockPos blockPos) {
        return this.getInChunk(worldId, ChunkPos.from(blockPos)).stream() // Get all claims of a chunk.
            .filter(claim -> claim.contains(blockPos)) // Ensure RegionClaims are actually contains the BlockPos.
            .collect(Collectors.toSet());
    }

    @Override
    public Set<T> getInChunk(Chunk chunk) {
        return this.getInChunk(chunk.getWorld(), chunk.getX(), chunk.getZ());
    }

    @Override
    public Set<T> getInChunk(World world, ChunkPos chunkPos) {
        return this.getInChunk(BukkitKeys.getKey(world), chunkPos.getX(), chunkPos.getZ());
    }

    @Override
    public Set<T> getInChunk(World world, int chunkX, int chunkZ) {
        return this.getInChunk(BukkitKeys.getKey(world), chunkX, chunkZ);
    }

    @Override
    public Set<T> getInChunk(AdaptedKey worldId, ChunkPos chunkPos) {
        return this.getInChunk(worldId, chunkPos.getX(), chunkPos.getZ());
    }

    @Override
    public Set<T> getInChunk(AdaptedKey worldId, int chunkX, int chunkZ) {
        long chunkKey = ChunkUtil.getChunkKey(chunkX, chunkZ);
        return this.getClaimsInChunk(worldId, chunkKey);
    }

    public Set<T> getClaimsInChunk(AdaptedKey worldId, long chunkKey) {
        Long2ObjectMap<Set<T>> spatialIndex = this.worldIndexes.get(worldId);
        if (spatialIndex == null) {
            return Set.of();
        }
        return spatialIndex.getOrDefault(chunkKey, Set.of());
    }

    @Override
    public Set<T> getByOwner(OfflinePlayer player) {
        return this.getByOwner(player.getUniqueId());
    }

    @Override
    public Set<T> getByOwner(UUID ownerId) {
        return Set.copyOf(this.byOwnerIdMap.getOrDefault(ownerId, Set.of()));
    }

    @Override
    public Set<T> getInCuboid(World world, Cuboid cuboid) {
        return this.getInCuboid(BukkitKeys.getKey(world), cuboid);
    }

    @Override
    public Set<T> getInCuboid(AdaptedKey worldId, Cuboid cuboid) {
        return cuboid.getIntersectingChunkPositions().stream()
            .flatMap(chunkPos -> this.getInChunk(worldId, chunkPos).stream()) // Get all claims of a cuboid chunk.
            .filter(claim -> claim.isIntersecting(cuboid)) // Ensure RegionClaims are actually intersects with the cuboid.
            .collect(Collectors.toSet());
    }

    @Override
    public @Nullable T getPrioritizedClaim(Block block) {
        return this.getPrioritizedClaim(block.getWorld(), BlockPos.from(block));
    }

    @Override
    public @Nullable T getPrioritizedClaim(Location location) {
        return this.getPrioritizedClaim(location.getWorld(), BlockPos.from(location));
    }

    @Override
    public @Nullable T getPrioritizedClaim(World world, BlockPos blockPos) {
        return this.getPrioritizedClaim(BukkitKeys.getKey(world), blockPos);
    }

    @Override
    public @Nullable T getPrioritizedClaim(AdaptedKey worldId, BlockPos blockPos) {
        return this.getAt(worldId, blockPos).stream().max(Comparator.comparingInt(Claim::getPriority)).orElse(null);
    }

    @Override
    public boolean isClaimed(Location location) {
        Claim claim = this.getPrioritizedClaim(location);
        return claim != null;
    }

    @Override
    public boolean isClaimed(Block block) {
        Claim claim = this.getPrioritizedClaim(block);
        return claim != null;
    }

    @Override
    public boolean isClaimed(World world, BlockPos blockPos) {
        return this.isClaimed(BukkitKeys.getKey(world), blockPos);
    }

    @Override
    public boolean isClaimed(AdaptedKey worldId, BlockPos blockPos) {
        Claim claim = this.getPrioritizedClaim(worldId, blockPos);
        return claim != null;
    }

    @Override
    public boolean isEmpty() {
        return this.registryMap.isEmpty();
    }

    @Override
    public boolean isEmpty(World world) {
        return this.isEmpty(BukkitKeys.getKey(world));
    }

    @Override
    public boolean isEmpty(AdaptedKey worldKey) {
        Long2ObjectMap<Set<T>> map = this.worldIndexes.get(worldKey);
        return map == null || map.isEmpty();
    }

    @Override
    public Set<String> idValues(World world) {
        return this.idValues(BukkitKeys.getKey(world));
    }

    @Override
    public Set<String> idValues(AdaptedKey worldKey) {
        return this.worldIndexes.keySet().stream().map(AdaptedKey::asString).collect(Collectors.toSet());
    }

    @Override
    public Set<Identifier> ids(World world) {
        return this.ids(BukkitKeys.getKey(world));
    }

    @Override
    public Set<Identifier> ids(AdaptedKey worldKey) {
        return this.stream(worldKey).map(Identifiable::id).collect(Collectors.toSet());
    }

    @Override
    public void reindex(@NonNull T claim) {
        this.remove(claim);
        this.register(claim);
    }

    @Override
    public void register(@NonNull T claim) {
        super.register(claim);

        AdaptedKey worldId = claim.getWorldKey();
        Long2ObjectMap<Set<T>> spatialIndex = this.worldIndexes
            .computeIfAbsent(worldId, k -> new Long2ObjectOpenHashMap<>());

        claim.getEffectiveChunkKeys().forEach(key -> {
            spatialIndex.computeIfAbsent(key, k -> new HashSet<>()).add(claim);
        });

        claim.getOwners().forEach(ownerId -> {
            this.byOwnerIdMap.computeIfAbsent(ownerId, k -> new HashSet<>()).add(claim);
        });
    }

    @Override
    public @Nullable T remove(@NonNull T claim) {
        T removed = super.remove(claim);

        AdaptedKey worldId = claim.getWorldKey();
        Long2ObjectMap<Set<T>> spatialIndex = this.worldIndexes
            .computeIfAbsent(worldId, k -> new Long2ObjectOpenHashMap<>());

        claim.getEffectiveChunkKeys().forEach(key -> {
            Set<T> claims = spatialIndex.get(key.longValue());
            if (claims != null) claims.remove(claim);
        });

        claim.getOwners().forEach(ownerId -> {
            Set<T> claims = this.byOwnerIdMap.get(ownerId);
            if (claims != null) claims.remove(claim);
        });

        return removed;
    }

    @Override
    public int size(World world) {
        return this.size(BukkitKeys.getKey(world));
    }

    @Override
    public int size(AdaptedKey worldKey) {
        return this.values(worldKey).size();
    }

    @Override
    public Stream<T> stream(World world) {
        return this.stream(BukkitKeys.getKey(world));
    }

    @Override
    public Stream<T> stream(AdaptedKey worldKey) {
        return this.worldLookup(worldKey)
            .map(Long2ObjectMap::values)
            .map(ObjectCollection::stream)
            .map(stream -> stream.flatMap(Set::stream))
            .orElse(Set.<T>of().stream());
    }

    @Override
    public Set<T> values(World world) {
        return this.values(BukkitKeys.getKey(world));
    }

    @Override
    public Set<T> values(AdaptedKey worldKey) {
        return this.worldLookup(worldKey)
            .map(Long2ObjectMap::values)
            .map(ObjectCollection::stream)
            .map(stream -> stream.flatMap(Set::stream))
            .map(flatStream -> flatStream.collect(Collectors.toSet()))
            .orElse(Set.of());
    }

    private Optional<Long2ObjectMap<Set<T>>> worldLookup(AdaptedKey worldKey) {
        return Optional.ofNullable(this.worldIndexes.get(worldKey));
    }
}
