package su.nightexpress.excellentclaims.land.placeholders;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.nightcore.util.NumberUtil;

public final class LandPlaceholdersConfiguration {

    private LandPlaceholdersConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        LandsRepository repository = container.get(LandsRepository.class);

        plugin.addGlobalPlaceholders(registry -> {
            registry.registerRaw("lands_claimed", (player, payload) -> {
                return NumberUtil.format(repository.countClaims(player));
            });
        });
    }
}
