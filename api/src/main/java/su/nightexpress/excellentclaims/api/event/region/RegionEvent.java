package su.nightexpress.excellentclaims.api.event.region;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.event.ClaimEvent;
import su.nightexpress.excellentclaims.api.region.Region;

@NullMarked
public abstract class RegionEvent extends ClaimEvent {

    protected final Region region;

    public RegionEvent(Region region) {
        super(region);
        this.region = region;
    }

    public Region getRegion() {
        return region;
    }
}
