package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

public final class DefaultRulesRegistrar {

    private DefaultRulesRegistrar() {
    }

    public static void register(DependencyContainer container) {
        RuleLoader loader = container.get(RuleLoader.class);
        RulesSettings settings = container.get(RulesSettings.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);

        EnvironmentRulesRegistrar.register(loader);

        BlockGrowRulesRegistrar.register(loader, settings);
        BlockFormRulesRegistrar.register(loader, settings);

        MobSpawnRulesRegistrar.register(loader);
        MobDamageRulesRegistrar.register(loader);
        MobGriefRulesRegistrar.register(loader);

        PlayerBlockRulesRegistrar.register(loader, permissions);
        PlayerItemRulesRegistrar.register(loader, permissions);
        PlayerDamageRulesRegistrar.register(loader, permissions);
        PlayerCommonRulesRegistrar.register(loader, settings, permissions);

        if (settings.isAllowHighFrequencyRules()) {
            HighFrequencyRulesRegistrar.register(loader, settings);
        }
    }
}
