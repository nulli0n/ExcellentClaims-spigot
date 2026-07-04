package su.nightexpress.excellentclaims.engine.controller;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.engine.ModuleRegistry;
import su.nightexpress.nightcore.bridge.BukkitKeys;

@NullMarked
public class WorldLifecycleController extends AbstractController {

    private final ModuleRegistry modules;

    public WorldLifecycleController(ClaimPlugin plugin, ModuleRegistry modules) {
        super(plugin);
        this.modules = modules;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {
        this.plugin.getServer().getWorlds().forEach(this::handleWorldLoad);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        this.handleWorldLoad(event.getWorld());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldUnload(WorldUnloadEvent event) {
        this.handleWorldUnload(event);
    }

    public void handleWorldLoad(World world) {
        this.modules.values().forEach(module -> {
            module.getDataService().loadClaims(BukkitKeys.getKey(world));
        });
    }

    public void handleWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();

        this.modules.values().forEach(module -> {
            module.getDataService().unloadClaims(BukkitKeys.getKey(world));
        });
    }
}
