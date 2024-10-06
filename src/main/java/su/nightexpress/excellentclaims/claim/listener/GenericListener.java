package su.nightexpress.excellentclaims.claim.listener;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.claim.ClaimManager;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.util.Relation;
import su.nightexpress.excellentclaims.util.RelationType;
import su.nightexpress.nightcore.language.message.LangMessage;
import su.nightexpress.nightcore.manager.AbstractListener;

public class GenericListener extends AbstractListener<ClaimPlugin> {

    private final ClaimManager manager;

    public GenericListener(@NotNull ClaimPlugin plugin, @NotNull ClaimManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();
        this.manager.getClaims(world).forEach(claim -> claim.activate(world));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();
        this.manager.getClaims(world).forEach(claim -> claim.deactivate(world));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;
        if (to.getX() == from.getX() && to.getZ() == from.getZ() && to.getY() == from.getY()) return;

        Player player = event.getPlayer();
        this.handleMovement(player, from, to);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        Player player = event.getPlayer();
        this.handleMovement(player, from, to);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        this.handleMovement(player, location, location);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location from = player.getLocation();
        Location to = event.getRespawnLocation();
        this.handleMovement(player, from, to);
    }

    private void handleMovement(@NotNull Player player, @NotNull Location from, @NotNull Location to) {
        Relation relation = this.manager.getRelation(from, to);
        if (relation.isEmpty()) return;

        RelationType type = relation.getType();
        LangMessage greetings;
        if (type == RelationType.TO_WILDERNESS) {
            greetings = Lang.GREETING_WILDERNESS.getMessage();
        }
        else if (type != RelationType.WILDERNESS && type != RelationType.INSIDE) {
            greetings = Lang.GREETING_CLAIM.getMessage().replace(relation.getTargetClaim().replacePlaceholders());
        }
        else return;

        greetings.send(player);
    }
}
