package su.nightexpress.excellentclaims.permission;

import su.nightexpress.excellentclaims.api.admin.AdminBypassAPI;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;

public final class PermissionConfiguration {

    private PermissionConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        AdminBypassAPI bypassAPI = container.getOrNull(AdminBypassAPI.class);
        RanksAPI ranksAPI = container.getOrNull(RanksAPI.class);

        PermissionService permissions = new PermissionService(ranksAPI, bypassAPI);

        container.register(ClaimPermissionAPI.class, permissions);
    }
}
