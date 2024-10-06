package su.nightexpress.excellentclaims.api.event.region;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.api.event.ClaimEvent;

public abstract class RegionEvent extends ClaimEvent {

    protected final RegionClaim regionClaim;

    public RegionEvent(@NotNull RegionClaim regionClaim) {
        super(regionClaim);
        this.regionClaim = regionClaim;
    }

    @NotNull
    public RegionClaim getRegionClaim() {
        return regionClaim;
    }
}
