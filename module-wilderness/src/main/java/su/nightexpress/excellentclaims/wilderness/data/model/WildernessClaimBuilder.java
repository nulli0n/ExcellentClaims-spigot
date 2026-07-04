package su.nightexpress.excellentclaims.wilderness.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.io.AbstractClaimBuilder;

@NullMarked
public class WildernessClaimBuilder extends AbstractClaimBuilder<WildernessRegion, WildernessClaimBuilder> {

    public WildernessClaimBuilder(DefaultClaimIdentity identity) {
        super(identity);
    }

    @Override
    public WildernessRegion build() {
        return new WildernessRegion(identity, definition, properties);
    }

    @Override
    protected WildernessClaimBuilder self() {
        return this;
    }
}
