package su.nightexpress.excellentclaims.api;

import org.bukkit.plugin.ServicePriority;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;

public final class APIConfiguration {

    private APIConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        APIContainer apiContainer = container.get(APIContainer.class);

        ClaimsAPI api = new ExcellentClaimsAPI(apiContainer);

        plugin.getServer().getServicesManager().register(ClaimsAPI.class, api, plugin, ServicePriority.Normal);
    }
}
