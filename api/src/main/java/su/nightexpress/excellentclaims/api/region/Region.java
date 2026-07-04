package su.nightexpress.excellentclaims.api.region;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.nightcore.util.geodata.Cuboid;

@NullMarked
public interface Region extends OwnableClaim {

    Cuboid getCuboid();

    RegionBoundingBox getBoundingBox();
}
