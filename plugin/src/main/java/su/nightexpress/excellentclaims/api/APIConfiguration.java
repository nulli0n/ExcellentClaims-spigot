package su.nightexpress.excellentclaims.api;

import org.bukkit.plugin.ServicePriority;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;

public final class APIConfiguration {

    private APIConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        //ClaimProtectionAPI protectionAPI = null; // TODO

        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimRegistry claims = container.get(ClaimRegistry.class);
        ClaimPermissionAPI permissionAPI = container.get(ClaimPermissionAPI.class);

        ClaimsAPI api = new ExcellentClaimsAPI(claims, permissionAPI);

        plugin.getServer().getServicesManager().register(ClaimsAPI.class, api, plugin, ServicePriority.Normal);
    }
}
