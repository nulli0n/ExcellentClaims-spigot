package su.nightexpress.excellentclaims.api.event.land;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.event.ClaimEvent;
import su.nightexpress.excellentclaims.api.land.Land;

@NullMarked
public abstract class LandEvent extends ClaimEvent {

    protected final Land land;

    public LandEvent(Land land) {
        super(land);
        this.land = land;
    }

    public Land getLand() {
        return land;
    }
}
