package su.nightexpress.excellentclaims.region.data;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.data.AbstractDataService;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.io.RegionIOService;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class RegionDataService extends AbstractDataService<RegionClaim> {

    public RegionDataService(RegionIOService ioService, RegionsRepository repository) {
        super(ioService, repository);
    }

    public RegionClaim createRegion(Identifier id, Player player, World world, Cuboid cuboid) {
        RegionClaim claim = this.ioService.createClaim(id, world);

        BlockPos center = cuboid.getCenter();

        claim.setSpawnLocation(ExactPos.from(world.getHighestBlockAt(center.getX(), center.getZ())));
        claim.addOwner(player);
        claim.getBoundingBox().setCuboid(cuboid);

        this.markDirty(claim);
        this.repository.register(claim);

        return claim;
    }
}
