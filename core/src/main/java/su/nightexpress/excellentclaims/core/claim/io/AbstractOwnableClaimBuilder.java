package su.nightexpress.excellentclaims.core.claim.io;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMembers;

@NullMarked
public abstract class AbstractOwnableClaimBuilder<T extends OwnableClaim, B extends AbstractOwnableClaimBuilder<T, B>> extends AbstractClaimBuilder<T, B> {

    protected DefaultClaimMembers members;

    public AbstractOwnableClaimBuilder(DefaultClaimIdentity identity) {
        super(identity);
        this.members = new DefaultClaimMembers();
    }

    public B members(DefaultClaimMembers members) {
        this.members = members;
        return this.self();
    }
}
