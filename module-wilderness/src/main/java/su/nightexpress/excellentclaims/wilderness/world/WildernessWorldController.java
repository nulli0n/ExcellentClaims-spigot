package su.nightexpress.excellentclaims.wilderness.world;

import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.wilderness.data.WildernessDataService;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.io.WildernessIOService;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class WildernessWorldController extends AbstractController {

    private final WildernessDataService dataService;
    private final WildernessIOService   ioService;
    private final WildernessSettings    settings;

    public WildernessWorldController(ClaimPlugin plugin,
                                     WildernessDataService dataService,
                                     WildernessIOService ioService,
                                     WildernessSettings settings) {
        super(plugin);
        this.dataService = dataService;
        this.ioService = ioService;
        this.settings = settings;
    }

    @Override
    protected void onReload() {
        this.checkWorlds();
    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {
        this.checkWorlds();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        this.ensureWildernessExists(world);
    }

    private void checkWorlds() {
        this.plugin.getServer().getWorlds().forEach(this::ensureWildernessExists);
    }

    private void ensureWildernessExists(World world) {
        AdaptedKey worldKey = BukkitKeys.getKey(world);
        Identifier id = Identifier.of(worldKey.value());

        Path file = this.ioService.getClaimFile(id.value(), worldKey);
        if (Files.exists(file)) return; // File exists, let global controller load it.

        this.plugin.info("Creating new wilderness region for " + worldKey);

        WildernessRegion region = this.dataService.createRegion(id, world);

        region.setName(this.settings.getWildernessDefaultName());
        region.setPriority(this.settings.getDefaultPriority());
    }
}
