package su.nightexpress.excellentclaims.api.claim;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public interface ClaimRepository<T extends Claim> {

    void reindex(@NonNull T claim);

    void register(@NonNull T item);

    @Nullable
    T remove(@NonNull T item);

    @Nullable
    T remove(Identifier id);

    @Nullable
    T get(Identifier key);

    Optional<T> lookup(Identifier key);

    Set<T> values();

    Set<T> values(World world);

    Set<T> values(AdaptedKey worldKey);

    Set<Identifier> ids();

    Set<Identifier> ids(World world);

    Set<Identifier> ids(AdaptedKey worldKey);

    Set<String> idValues();

    Set<String> idValues(World world);

    Set<String> idValues(AdaptedKey worldKey);

    void clear();

    void clear(World world);

    void clear(AdaptedKey worldKey);

    int size();

    int size(World world);

    int size(AdaptedKey worldKey);

    boolean isEmpty();

    boolean isEmpty(World world);

    boolean isEmpty(AdaptedKey worldKey);


    int countClaims(Player player);

    int countClaims(UUID playerId);


    boolean isClaimed(Location location);

    boolean isClaimed(Block block);

    boolean isClaimed(World world, BlockPos blockPos);

    boolean isClaimed(AdaptedKey worldId, BlockPos blockPos);


    Stream<T> stream();

    Stream<T> stream(World world);

    Stream<T> stream(AdaptedKey worldKey);


    Set<T> getByOwner(OfflinePlayer player);

    Set<T> getByOwner(UUID ownerId);


    Set<T> getInCuboid(World world, Cuboid cuboid);

    Set<T> getInCuboid(AdaptedKey worldId, Cuboid cuboid);

    Set<T> getAt(Location location);

    Set<T> getAt(Block block);

    Set<T> getAt(World world, BlockPos blockPos);

    Set<T> getAt(AdaptedKey worldId, BlockPos blockPos);

    Set<T> getInChunk(Chunk chunk);

    Set<T> getInChunk(World world, ChunkPos chunkPos);

    Set<T> getInChunk(World world, int chunkX, int chunkZ);

    Set<T> getInChunk(AdaptedKey worldId, ChunkPos chunkPos);

    Set<T> getInChunk(AdaptedKey worldId, int chunkX, int chunkZ);


    @Nullable
    T getPrioritizedClaim(Block block);

    @Nullable
    T getPrioritizedClaim(Location location);

    @Nullable
    T getPrioritizedClaim(World world, BlockPos blockPos);

    @Nullable
    T getPrioritizedClaim(AdaptedKey worldId, BlockPos blockPos);
}
