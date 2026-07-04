package su.nightexpress.excellentclaims.core.claim;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimIdentity;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public record DefaultClaimIdentity(Identifier idKey, AdaptedKey worldKey,
                                   Identifier moduleKey) implements ClaimIdentity {

}
