package su.nightexpress.excellentclaims.api.event.land;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.land.Land;

@NullMarked
public abstract class PlayerLandEvent extends LandEvent {

    protected final Player player;

    public PlayerLandEvent(Land land, Player player) {
        super(land);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
