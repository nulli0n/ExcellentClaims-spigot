package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.region.Region;

@NullMarked
public class RegionRemoveEvent extends PlayerRegionEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;

    public RegionRemoveEvent(Region region, Player player) {
        super(region, player);
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
