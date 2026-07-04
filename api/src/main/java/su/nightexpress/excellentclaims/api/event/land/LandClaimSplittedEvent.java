package su.nightexpress.excellentclaims.api.event.land;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.land.Land;

@NullMarked
public class LandClaimSplittedEvent extends PlayerLandEvent {

    private static final HandlerList handlers = new HandlerList();

    private final Land target;

    public LandClaimSplittedEvent(Land source, Player player, Land target) {
        super(source, player);
        this.target = target;
    }

    public Land getSource() {
        return this.getLand();
    }

    public Land getTarget() {
        return this.target;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
