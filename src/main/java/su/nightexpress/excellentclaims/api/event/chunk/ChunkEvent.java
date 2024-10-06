package su.nightexpress.excellentclaims.api.event.chunk;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;
import su.nightexpress.excellentclaims.api.event.ClaimEvent;

public abstract class ChunkEvent extends ClaimEvent {

    protected final ChunkClaim chunkClaim;

    public ChunkEvent(@NotNull ChunkClaim chunkClaim) {
        super(chunkClaim);
        this.chunkClaim = chunkClaim;
    }

    @NotNull
    public ChunkClaim getChunkClaim() {
        return chunkClaim;
    }
}
