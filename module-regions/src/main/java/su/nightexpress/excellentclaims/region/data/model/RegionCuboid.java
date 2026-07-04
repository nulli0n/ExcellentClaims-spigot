package su.nightexpress.excellentclaims.region.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.region.RegionBoundingBox;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class RegionCuboid implements RegionBoundingBox {

    private Cuboid cuboid;

    @Deprecated // TODO EmptyCuboid impl
    public RegionCuboid() {
        this(new Cuboid(BlockPos.empty(), BlockPos.empty()));
    }

    public RegionCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }

    @Override
    public Cuboid getCuboid() {
        return this.cuboid;
    }

    @Override
    public void setCuboid(Cuboid cuboid) {
        this.cuboid = cuboid;
    }
}
