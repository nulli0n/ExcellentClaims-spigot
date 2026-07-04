package su.nightexpress.excellentclaims.engine.controller;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class ClaimGreetingsController extends AbstractController {

    private final ClaimRegistry registry;

    public ClaimGreetingsController(ClaimPlugin plugin, ClaimRegistry registry) {
        super(plugin);
        this.registry = registry;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        if (to == null) return;

        Location from = event.getFrom();
        if (to.getX() == from.getX() && to.getZ() == from.getZ() && to.getY() == from.getY()) return;

        Player player = event.getPlayer();
        this.handleMovement(player, from, to);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location to = event.getTo();
        if (to == null) return;

        Location from = event.getFrom();
        Player player = event.getPlayer();
        this.handleMovement(player, from, to);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (location == null) return;

        this.handleMovement(player, location, location);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location from = player.getLocation();
        if (from == null) return;

        Location to = event.getRespawnLocation();
        this.handleMovement(player, from, to);
    }

    private void handleMovement(Player player, Location from, Location to) {
        Claim currentClaim = this.registry.getPrioritizedClaim(from);
        Claim nextClaim = this.registry.getPrioritizedClaim(to);

        if (nextClaim == null) return;
        if (currentClaim == nextClaim) return;

        Lang.GREETING_CLAIM.message().sendWith(player, ctx -> ctx
            .with(nextClaim.placeholders())
            .with(CommonPlaceholders.GENERIC_NAME, nextClaim::getName)
            .with(CommonPlaceholders.GENERIC_DESCRIPTION, () -> String.join("\n", nextClaim.getDescription()))
        );
    }
}
