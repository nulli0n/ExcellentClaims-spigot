package su.nightexpress.excellentclaims.api.event.chunk;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.LandClaim;
import su.nightexpress.excellentclaims.api.event.ClaimEvent;

public abstract class ChunkEvent extends ClaimEvent {

    protected final LandClaim landClaim;

    public ChunkEvent(@NotNull LandClaim landClaim) {
        super(landClaim);
        this.landClaim = landClaim;
    }

    @NotNull
    public LandClaim getChunkClaim() {
        return landClaim;
    }
}
