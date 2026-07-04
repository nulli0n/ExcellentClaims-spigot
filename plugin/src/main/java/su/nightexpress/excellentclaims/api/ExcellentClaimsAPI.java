package su.nightexpress.excellentclaims.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;

@NullMarked
public class ExcellentClaimsAPI implements ClaimsAPI {

    private final ClaimRegistry      claimRegistry;
    private final ClaimPermissionAPI permissions;

    public ExcellentClaimsAPI(ClaimRegistry claimRegistry, ClaimPermissionAPI permissions) {
        this.claimRegistry = claimRegistry;
        this.permissions = permissions;
    }

    @Override
    public ClaimRegistry getClaimRegistry() {
        return this.claimRegistry;
    }

    public ClaimPermissionAPI getPermissionAPI() {
        return permissions;
    }
}
