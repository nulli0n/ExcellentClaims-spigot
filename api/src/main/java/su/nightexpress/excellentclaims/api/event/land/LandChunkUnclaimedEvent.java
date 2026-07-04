package su.nightexpress.excellentclaims.api.event.land;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.land.Land;

@NullMarked
public class LandChunkUnclaimedEvent extends PlayerLandEvent {

    private static final HandlerList handlers = new HandlerList();

    public LandChunkUnclaimedEvent(Land land, Player player) {
        super(land, player);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
