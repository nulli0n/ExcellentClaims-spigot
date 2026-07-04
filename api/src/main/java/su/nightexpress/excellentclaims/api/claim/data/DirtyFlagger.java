package su.nightexpress.excellentclaims.api.claim.data;

import org.jspecify.annotations.NonNull;

import su.nightexpress.excellentclaims.api.claim.Claim;

public interface DirtyFlagger<T extends Claim> {

    void markDirty(@NonNull T claim);
}
