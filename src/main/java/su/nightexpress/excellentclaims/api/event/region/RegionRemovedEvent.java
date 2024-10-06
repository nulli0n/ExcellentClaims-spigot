package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;

public class RegionRemovedEvent extends PlayerRegionEvent {

    private static final HandlerList handlers = new HandlerList();

    public RegionRemovedEvent(@NotNull RegionClaim regionClaim, @NotNull Player player) {
        super(regionClaim, player);
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
