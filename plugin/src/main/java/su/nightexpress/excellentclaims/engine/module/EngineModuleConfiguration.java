package su.nightexpress.excellentclaims.engine.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModuleBootstrap;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.premium.PremiumFeatures;
import su.nightexpress.excellentclaims.engine.ClaimEngine;
import su.nightexpress.excellentclaims.engine.ModuleRegistry;
import su.nightexpress.excellentclaims.engine.settings.EngineSettings;
import su.nightexpress.excellentclaims.land.LandsBootstrap;

@NullMarked
public final class EngineModuleConfiguration {

    private EngineModuleConfiguration() {
    }

    public static void configure(ClaimEngine engine, DependencyContainer container) {
        ClaimRegistry claims = container.get(ClaimRegistry.class);
        ModuleRegistry modules = container.get(ModuleRegistry.class);
        EngineSettings settings = container.get(EngineSettings.class);
        PremiumFeatures features = container.get(PremiumFeatures.class);

        EngineModuleLoader moduleLoader = new EngineModuleLoader(modules, claims);

        // Define Available Bootstraps
        List<ClaimModuleBootstrap> availableBootstraps = new ArrayList<>();
        availableBootstraps.add(new LandsBootstrap());

        if (features.isPremium()) {
            availableBootstraps.addAll(EngineModuleBootstrapProvider.provide());
        }

        // TODO ModuleConfigurationEvent with a list

        Set<String> disabledModules = settings.getDisabledClaimModules();

        for (ClaimModuleBootstrap bootstrap : availableBootstraps) {
            if (disabledModules.contains(bootstrap.getId())) {
                continue;
            }

            ClaimModule module = bootstrap.bootstrap(container);

            moduleLoader.registerModule(module);
        }

        engine.addComponent(moduleLoader);
    }
}