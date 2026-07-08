package su.nightexpress.excellentclaims.engine;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.premium.PremiumFeatures;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.engine.command.ReloadCommand;
import su.nightexpress.excellentclaims.engine.controller.ClaimAutoSaveController;
import su.nightexpress.excellentclaims.engine.controller.ClaimGreetingsController;
import su.nightexpress.excellentclaims.engine.controller.WorldLifecycleController;
import su.nightexpress.excellentclaims.engine.module.EngineModuleConfiguration;
import su.nightexpress.excellentclaims.engine.placeholders.EnginePlaceholderConfiguration;
import su.nightexpress.excellentclaims.engine.settings.EngineSettings;

@NullMarked
public final class EngineBootstrap {

    private static final String CONFIG_FILE = "engine.yml";

    private EngineBootstrap() {
    }

    public static ClaimEngine bootstrap(DependencyContainer container) {
        DependencyContainer engineContainer = new SimpleDependencies(container);

        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimRegistry claims = container.get(ClaimRegistry.class);
        PremiumFeatures features = container.get(PremiumFeatures.class);

        ModuleRegistry modules = new ModuleRegistry();
        ClaimEngine engine = new ClaimEngine(plugin, claims, modules);

        container.register(ModuleRegistry.class, modules);

        EngineCodecsRegistrar.register();
        EngineSettings settings = configureSettings(engine, engineContainer);
        EngineModuleConfiguration.configure(engine, engineContainer);

        if (features.isPremium()) {
            EnginePlaceholderConfiguration.configure(engineContainer);
        }

        engine.addComponent(new ReloadCommand(plugin));
        engine.addComponent(new ClaimAutoSaveController(plugin, modules, settings));
        engine.addComponent(new ClaimGreetingsController(plugin, claims));
        engine.addComponent(new WorldLifecycleController(plugin, modules));

        return engine;
    }

    private static EngineSettings configureSettings(ClaimEngine engine, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        Path filePath = plugin.dataPath().resolve(ClaimsConstants.DIR_CONFIG).resolve(CONFIG_FILE);

        return SettingsFactory.create(engine, container, filePath, EngineSettings::new);
    }
}
