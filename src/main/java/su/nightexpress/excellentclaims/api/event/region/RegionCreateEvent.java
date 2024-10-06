package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.util.Cuboid;
import su.nightexpress.nightcore.util.StringUtil;

public class RegionCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final World world;
    private final Cuboid cuboid;

    private String name;
    private boolean cancelled;

    public RegionCreateEvent(@NotNull Player player, @NotNull World world, @NotNull Cuboid cuboid, @NotNull String name) {
        this.player = player;
        this.world = world;
        this.cuboid = cuboid;
        this.name = name;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public World getWorld() {
        return world;
    }

    @NotNull
    public Cuboid getCuboid() {
        return cuboid;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Sets region name (unique identifier).
     * Only latin letters, numbers and an underscore are allowed.
     * @param name Region name (unique identifier).
     */
    public void setName(@NotNull String name) {
        name = StringUtil.lowerCaseUnderscoreStrict(name);
        if (!name.isBlank()) {
            this.name = name;
        }
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
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
