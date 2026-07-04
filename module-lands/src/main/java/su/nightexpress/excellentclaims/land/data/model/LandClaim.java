package su.nightexpress.excellentclaims.land.data.model;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimDefinition;
import su.nightexpress.excellentclaims.api.claim.ClaimIdentity;
import su.nightexpress.excellentclaims.api.claim.ClaimMembers;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.land.Land;
import su.nightexpress.excellentclaims.api.land.LandChunks;
import su.nightexpress.excellentclaims.core.claim.base.AbstractOwnedClaim;
import su.nightexpress.excellentclaims.core.util.ChunkUtil;
import su.nightexpress.excellentclaims.land.LandsPlaceholders;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class LandClaim extends AbstractOwnedClaim implements Land {

    private final LandChunks chunkData;

    public LandClaim(ClaimIdentity identity,
                     ClaimDefinition definition,
                     ClaimRules properties,
                     ClaimMembers members,
                     LandChunks chunkData) {
        super(identity, definition, properties, members);
        this.chunkData = chunkData;
    }

    @Override
    public PlaceholderResolver placeholders() {
        return LandsPlaceholders.LAND.resolver(this);
    }

    @Override
    public boolean contains(Location location) {
        World world = location.getWorld();
        if (world == null || !this.isWorld(world)) return false;

        return this.contains(BlockPos.from(location));
    }

    @Override
    public boolean contains(BlockPos blockPos) {
        return this.chunkData.contains(ChunkPos.from(blockPos));
    }

    @Override
    public boolean contains(ChunkPos chunkPos) {
        return this.chunkData.contains(chunkPos);
    }

    @Override
    public boolean isBackgroundClaim() {
        return false;
    }

    @Override
    public boolean isSupportingUnsetRules() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.chunkData.isEmpty();
    }

    @Override
    public boolean isMerged() {
        return this.getChunksAmount() > 1;
    }

    @Override
    public boolean isSingle() {
        return this.getChunksAmount() == 1;
    }

    @Override
    public int getChunksAmount() {
        return this.chunkData.size();
    }

    @Override
    public Set<ChunkPos> getPositions() {
        return this.chunkData.getChunkPositions();
    }

    @Override
    public Set<Long> getEffectiveChunkKeys() {
        return this.getPositions().stream().mapToLong(ChunkUtil::getChunkKey).boxed().collect(Collectors.toSet());
    }

    @Override
    public boolean isIntersecting(Cuboid cuboid) {
        return cuboid.getIntersectingChunkPositions().stream().anyMatch(this.chunkData::contains);
    }

    @Override
    public Set<Chunk> getChunks() {
        return this.world()
            .map(world -> this.getPositions().stream().map(pos -> pos.getChunk(world)).collect(Collectors.toSet()))
            .orElse(Set.of());
    }

    @Override
    public LandChunks getChunkData() {
        return chunkData;
    }
}
