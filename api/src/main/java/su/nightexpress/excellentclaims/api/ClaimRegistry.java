package su.nightexpress.excellentclaims.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimRepository;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class ClaimRegistry {

    private final List<ClaimRepository<? extends Claim>> repositories;

    public ClaimRegistry() {
        this.repositories = new ArrayList<>();
    }

    public void registerRepository(ClaimRepository<? extends Claim> repository) {
        this.repositories.add(repository);
    }

    public List<ClaimRepository<? extends Claim>> getRepositories() {
        return List.copyOf(this.repositories);
    }

    // -----------------------------------------------------------------------
    // Mutators (Routed to specific repositories)
    // -----------------------------------------------------------------------

    public void clear() {
        this.repositories.forEach(ClaimRepository::clear);
    }

    public void clear(World world) {
        this.repositories.forEach(repo -> repo.clear(world));
    }

    public void clear(AdaptedKey worldKey) {
        this.repositories.forEach(repo -> repo.clear(worldKey));
    }

    // -----------------------------------------------------------------------
    // Read Operations (Aggregated)
    // -----------------------------------------------------------------------

    public int countClaims(Player player) {
        return this.countClaims(player.getUniqueId());
    }

    public int countClaims(UUID playerId) {
        return this.repositories.stream()
            .mapToInt(repo -> repo.countClaims(playerId))
            .sum();
    }

    public Set<Claim> getAt(Location location) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getAt(location).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getAt(Block block) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getAt(block).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getAt(World world, BlockPos blockPos) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getAt(world, blockPos).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getAt(AdaptedKey worldId, BlockPos blockPos) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getAt(worldId, blockPos).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInChunk(Chunk chunk) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInChunk(chunk).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInChunk(World world, ChunkPos chunkPos) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInChunk(world, chunkPos).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInChunk(World world, int chunkX, int chunkZ) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInChunk(world, chunkX, chunkZ).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInChunk(AdaptedKey worldId, ChunkPos chunkPos) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInChunk(worldId, chunkPos).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInChunk(AdaptedKey worldId, int chunkX, int chunkZ) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInChunk(worldId, chunkX, chunkZ).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getByOwner(OfflinePlayer player) {
        return this.getByOwner(player.getUniqueId());
    }

    public Set<Claim> getByOwner(UUID ownerId) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getByOwner(ownerId).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInCuboid(World world, Cuboid cuboid) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInCuboid(world, cuboid).stream())
            .collect(Collectors.toSet());
    }

    public Set<Claim> getInCuboid(AdaptedKey worldId, Cuboid cuboid) {
        return this.repositories.stream()
            .flatMap(repo -> repo.getInCuboid(worldId, cuboid).stream())
            .collect(Collectors.toSet());
    }

    // -----------------------------------------------------------------------
    // Prioritization & Boolean Checks
    // -----------------------------------------------------------------------

    public @Nullable Claim getPrioritizedClaim(Player player) {
        Location location = player.getLocation();
        return location == null ? null : this.getPrioritizedClaim(location);
    }

    public @Nullable Claim getPrioritizedClaim(Block block) {
        return this.getPrioritizedClaim(this.getAt(block));
    }

    public @Nullable Claim getPrioritizedClaim(Location location) {
        return this.getPrioritizedClaim(this.getAt(location));
    }

    public @Nullable Claim getPrioritizedClaim(World world, BlockPos blockPos) {
        return this.getPrioritizedClaim(this.getAt(world, blockPos));
    }

    public @Nullable Claim getPrioritizedClaim(AdaptedKey worldId, BlockPos blockPos) {
        return this.getPrioritizedClaim(this.getAt(worldId, blockPos));
    }

    private @Nullable Claim getPrioritizedClaim(Set<Claim> claims) {
        return claims.stream()
            .max(Comparator.comparingInt(Claim::getPriority))
            .orElse(null);
    }

    public boolean isClaimed(Location location) {
        return this.repositories.stream().anyMatch(repo -> repo.isClaimed(location));
    }

    public boolean isClaimed(Block block) {
        return this.repositories.stream().anyMatch(repo -> repo.isClaimed(block));
    }

    public boolean isClaimed(World world, BlockPos blockPos) {
        return this.repositories.stream().anyMatch(repo -> repo.isClaimed(world, blockPos));
    }

    public boolean isClaimed(AdaptedKey worldId, BlockPos blockPos) {
        return this.repositories.stream().anyMatch(repo -> repo.isClaimed(worldId, blockPos));
    }

    // -----------------------------------------------------------------------
    // State & Sizing
    // -----------------------------------------------------------------------

    public boolean isEmpty() {
        return this.repositories.stream().allMatch(ClaimRepository::isEmpty);
    }

    public boolean isEmpty(World world) {
        return this.repositories.stream().allMatch(repo -> repo.isEmpty(world));
    }

    public boolean isEmpty(AdaptedKey worldKey) {
        return this.repositories.stream().allMatch(repo -> repo.isEmpty(worldKey));
    }

    public int size(World world) {
        return this.repositories.stream().mapToInt(repo -> repo.size(world)).sum();
    }

    public int size(AdaptedKey worldKey) {
        return this.repositories.stream().mapToInt(repo -> repo.size(worldKey)).sum();
    }

    // -----------------------------------------------------------------------
    // Streams & Values
    // -----------------------------------------------------------------------

    public Stream<? extends Claim> stream(World world) {
        return this.repositories.stream().flatMap(repo -> repo.stream(world));
    }

    public Stream<? extends Claim> stream(AdaptedKey worldKey) {
        return this.repositories.stream().flatMap(repo -> repo.stream(worldKey));
    }

    public Set<Claim> values(World world) {
        return this.stream(world).collect(Collectors.toSet());
    }

    public Set<Claim> values(AdaptedKey worldKey) {
        return this.stream(worldKey).collect(Collectors.toSet());
    }
}