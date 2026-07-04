package su.nightexpress.excellentclaims.wilderness.data;

import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.claim.data.AbstractDataService;
import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.io.WildernessIOService;
import su.nightexpress.nightcore.util.geodata.pos.ExactPos;

@NullMarked
public class WildernessDataService extends AbstractDataService<WildernessRegion> {

    public WildernessDataService(WildernessIOService ioService, WildernessRepository repository) {
        super(ioService, repository);
    }

    public WildernessRegion createRegion(Identifier id, World world) {
        WildernessRegion region = this.ioService.createClaim(id, world);

        region.setSpawnLocation(ExactPos.from(world.getSpawnLocation()));

        this.markDirty(region);
        this.repository.register(region);

        return region;
    }
}
