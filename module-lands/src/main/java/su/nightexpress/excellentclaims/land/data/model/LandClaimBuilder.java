package su.nightexpress.excellentclaims.land.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.io.AbstractOwnableClaimBuilder;

@NullMarked
public class LandClaimBuilder extends AbstractOwnableClaimBuilder<LandClaim, LandClaimBuilder> {

    private LandChunkData chunkData = new LandChunkData();

    public LandClaimBuilder(DefaultClaimIdentity identity) {
        super(identity);
    }

    @Override
    public LandClaim build() {
        return new LandClaim(identity, definition, properties, members, chunkData);
    }

    @Override
    protected LandClaimBuilder self() {
        return this;
    }

    public LandClaimBuilder chunkData(LandChunkData chunkData) {
        this.chunkData = chunkData;
        return this;
    }
}
