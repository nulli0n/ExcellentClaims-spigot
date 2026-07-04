package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.util.geodata.Cuboid;

@NullMarked
public class RegionCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player     player;
    private final World      world;
    private final Cuboid     cuboid;
    private final Identifier name;

    private boolean cancelled;

    public RegionCreateEvent(Player player, World world, Cuboid cuboid, Identifier name) {
        super();
        this.player = player;
        this.world = world;
        this.cuboid = cuboid;
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public Identifier getName() {
        return name;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
