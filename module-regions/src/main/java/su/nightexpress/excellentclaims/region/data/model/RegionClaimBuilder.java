package su.nightexpress.excellentclaims.region.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.io.AbstractOwnableClaimBuilder;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class RegionClaimBuilder extends AbstractOwnableClaimBuilder<RegionClaim, RegionClaimBuilder> {

    private RegionCuboid cuboid = new RegionCuboid(new Cuboid(BlockPos.empty(), BlockPos.empty()));

    public RegionClaimBuilder(DefaultClaimIdentity identity) {
        super(identity);
    }

    @Override
    public RegionClaim build() {
        return new RegionClaim(identity, definition, properties, members, cuboid);
    }

    @Override
    protected RegionClaimBuilder self() {
        return this;
    }

    public RegionClaimBuilder boundingBox(RegionCuboid cuboid) {
        this.cuboid = cuboid;
        return this;
    }
}
