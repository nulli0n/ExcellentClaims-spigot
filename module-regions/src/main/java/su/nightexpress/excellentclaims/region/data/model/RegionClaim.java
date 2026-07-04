package su.nightexpress.excellentclaims.region.data.model;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimDefinition;
import su.nightexpress.excellentclaims.api.claim.ClaimIdentity;
import su.nightexpress.excellentclaims.api.claim.ClaimMembers;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.region.Region;
import su.nightexpress.excellentclaims.api.region.RegionBoundingBox;
import su.nightexpress.excellentclaims.core.claim.base.AbstractOwnedClaim;
import su.nightexpress.excellentclaims.core.util.ChunkUtil;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class RegionClaim extends AbstractOwnedClaim implements Region {

    private final RegionBoundingBox boundingBox;

    public RegionClaim(ClaimIdentity identity,
                       ClaimDefinition definition,
                       ClaimRules properties,
                       ClaimMembers members,
                       RegionBoundingBox boundingBox) {
        super(identity, definition, properties, members);
        this.boundingBox = boundingBox;
    }

    @Override
    public PlaceholderResolver placeholders() {
        return RegionsPlaceholders.REGION.resolver(this);
    }

    @Override
    public boolean contains(Location location) {
        World world = location.getWorld();
        if (world == null || !this.isWorld(world)) return false;

        return this.contains(BlockPos.from(location));
    }

    @Override
    public boolean contains(BlockPos blockPos) {
        return this.getCuboid().contains(blockPos);
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
        return this.getCuboid().isEmpty();
    }

    @Override
    public Set<Long> getEffectiveChunkKeys() {
        return this.getCuboid().getIntersectingChunkPositions()
            .stream()
            .mapToLong(ChunkUtil::getChunkKey)
            .boxed()
            .collect(Collectors.toSet());
    }

    @Override
    public boolean isIntersecting(Cuboid cuboid) {
        return this.getCuboid().isIntersectingWith(cuboid);
    }

    @Override
    public Cuboid getCuboid() {
        return this.boundingBox.getCuboid();
    }

    @Override
    public RegionBoundingBox getBoundingBox() {
        return this.boundingBox;
    }
}
