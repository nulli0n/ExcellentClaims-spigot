package su.nightexpress.excellentclaims.api.region;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.Cuboid;

@NullMarked
public interface RegionBoundingBox {

    Cuboid getCuboid();

    void setCuboid(Cuboid cuboid);
}
