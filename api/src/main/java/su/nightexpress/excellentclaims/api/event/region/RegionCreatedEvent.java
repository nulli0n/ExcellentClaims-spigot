package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.region.Region;

@NullMarked
public class RegionCreatedEvent extends PlayerRegionEvent {

    private static final HandlerList handlers = new HandlerList();

    public RegionCreatedEvent(Region region, Player player) {
        super(region, player);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
