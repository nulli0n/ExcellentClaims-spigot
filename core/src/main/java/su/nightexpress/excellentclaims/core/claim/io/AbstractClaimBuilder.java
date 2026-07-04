package su.nightexpress.excellentclaims.core.claim.io;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimDefinition;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimIdentity;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimRules;

@NullMarked
public abstract class AbstractClaimBuilder<T extends Claim, B extends AbstractClaimBuilder<T, B>> {

    protected final DefaultClaimIdentity identity;
    protected DefaultClaimDefinition     definition;
    protected DefaultClaimRules          properties;

    public AbstractClaimBuilder(DefaultClaimIdentity identity) {
        this.identity = identity;
        this.definition = new DefaultClaimDefinition();
        this.properties = new DefaultClaimRules();
    }

    public B definition(DefaultClaimDefinition definition) {
        this.definition = definition;
        return this.self();
    }

    public B properties(DefaultClaimRules properties) {
        this.properties = properties;
        return this.self();
    }

    protected abstract B self();

    public abstract T build();
}
