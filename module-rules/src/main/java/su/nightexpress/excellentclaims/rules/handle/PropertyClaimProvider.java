package su.nightexpress.excellentclaims.rules.handle;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;

@FunctionalInterface
@NullMarked
public interface PropertyClaimProvider<E> {

    @Nullable
    Claim getClaim(E event, ClaimRegistry registry);
}
