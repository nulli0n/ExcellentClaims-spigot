package su.nightexpress.excellentclaims.api.claim;

import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

public interface ClaimIdentity {

    Identifier idKey();

    AdaptedKey worldKey();

    Identifier moduleKey();
}
