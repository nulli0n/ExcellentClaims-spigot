package su.nightexpress.excellentclaims.region.placeholders;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.nightcore.util.NumberUtil;

public final class RegionPlaceholdersConfiguration {

    private RegionPlaceholdersConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        RegionsRepository repository = container.get(RegionsRepository.class);

        plugin.addGlobalPlaceholders(registry -> {
            registry.registerRaw("regions_claimed", (player, payload) -> {
                return NumberUtil.format(repository.countClaims(player));
            });
        });
    }
}
