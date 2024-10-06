package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;

public abstract class PlayerRegionEvent extends RegionEvent {

    protected final Player player;

    public PlayerRegionEvent(@NotNull RegionClaim regionClaim, @NotNull Player player) {
        super(regionClaim);
        this.player = player;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }
}
