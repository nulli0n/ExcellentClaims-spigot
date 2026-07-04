package su.nightexpress.excellentclaims.api.event.land;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.land.Land;

@NullMarked
public class LandClaimMergeEvent extends PlayerLandEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Land target;

    private boolean cancelled;

    public LandClaimMergeEvent(Land source, Player player, Land target) {
        super(source, player);
        this.target = target;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
