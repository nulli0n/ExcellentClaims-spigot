package su.nightexpress.excellentclaims.wilderness.data.model;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimDefinition;
import su.nightexpress.excellentclaims.api.claim.ClaimIdentity;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.wilderness.Wilderness;
import su.nightexpress.excellentclaims.core.claim.base.AbstractClaim;
import su.nightexpress.excellentclaims.wilderness.WildernessPlaceholders;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class WildernessRegion extends AbstractClaim implements Wilderness {

    public WildernessRegion(ClaimIdentity identity,
                            ClaimDefinition definition,
                            ClaimRules properties) {
        super(identity, definition, properties);
    }

    @Override
    public PlaceholderResolver placeholders() {
        return WildernessPlaceholders.WILDERNESS.resolver(this);
    }

    @Override
    public boolean contains(Location location) {
        World world = location.getWorld();
        return world != null && this.isWorld(world);
    }

    @Override
    public boolean contains(BlockPos blockPos) {
        return true;
    }

    @Override
    public boolean isBackgroundClaim() {
        return true;
    }

    @Override
    public boolean isSupportingUnsetRules() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<Long> getEffectiveChunkKeys() {
        return Set.of();
    }

    @Override
    public boolean isIntersecting(Cuboid cuboid) {
        return true;
    }
}
