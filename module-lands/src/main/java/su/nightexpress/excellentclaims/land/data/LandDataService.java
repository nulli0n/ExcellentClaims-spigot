package su.nightexpress.excellentclaims.land.data;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.data.AbstractDataService;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.io.LandIOService;
import su.nightexpress.nightcore.util.geodata.GeoUtils;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class LandDataService extends AbstractDataService<LandClaim> {

    public LandDataService(LandIOService ioService, LandsRepository repository) {
        super(ioService, repository);
    }

    public LandClaim createClaim(Identifier id, Player player, World world, ChunkPos chunkPos) {
        LandClaim claim = this.ioService.createClaim(id, world);

        int blockX = GeoUtils.shiftToCoord(chunkPos.getX());
        int blockZ = GeoUtils.shiftToCoord(chunkPos.getZ());

        claim.setSpawnLocation(ExactPos.from(world.getHighestBlockAt(blockX, blockZ)));
        claim.addOwner(player);
        claim.getChunkData().addChunkPosition(chunkPos);

        this.markDirty(claim);
        this.repository.register(claim);

        return claim;
    }
}
