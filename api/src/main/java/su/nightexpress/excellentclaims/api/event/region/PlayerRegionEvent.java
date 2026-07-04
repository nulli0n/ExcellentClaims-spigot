package su.nightexpress.excellentclaims.api.event.region;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.region.Region;

@NullMarked
public abstract class PlayerRegionEvent extends RegionEvent {

    protected final Player player;

    public PlayerRegionEvent(Region region, Player player) {
        super(region);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
