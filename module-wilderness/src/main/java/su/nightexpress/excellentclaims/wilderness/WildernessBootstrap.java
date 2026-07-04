package su.nightexpress.excellentclaims.wilderness;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModuleBootstrap;
import su.nightexpress.excellentclaims.api.core.ConfigurableModule;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.core.settings.SettingsController;
import su.nightexpress.excellentclaims.core.StandardMessageDispatcher;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandConfiguration;
import su.nightexpress.excellentclaims.wilderness.data.WildernessDataService;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorConfiguration;
import su.nightexpress.excellentclaims.wilderness.io.WildernessIOService;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.excellentclaims.wilderness.permission.WildernessPerms;
import su.nightexpress.excellentclaims.wilderness.rules.WildernessRulesConfiguration;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIConfiguration;
import su.nightexpress.excellentclaims.wilderness.world.WildernessWorldController;

@NullMarked
public class WildernessBootstrap implements ClaimModuleBootstrap {

    private static final Identifier ID = Identifier.of("wilderness");

    private static final String FILE_CONFIG = "core.yml";
    private static final String DIR_WILDS   = "wilderness";
    private static final String DIR_CONFIG  = "wilderness";

    @Override
    public String getId() {
        return "wilderness";
    }

    @Override
    public ClaimModule bootstrap(DependencyContainer container) {
        DependencyContainer regionsContainer = new SimpleDependencies(container);

        //EventPublisher eventPublisher = container.get(EventPublisher.class);
        Logger logger = container.get(Logger.class);
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        Path regionsDir = plugin.dataPath().resolve(DIR_WILDS);
        Path moduleDir = plugin.dataPath().resolve(ClaimsConstants.DIR_CONFIG).resolve(DIR_CONFIG);

        // Inject Lang & Permission before service configuration
        plugin.injectLang(WildernessLang.class);
        plugin.registerPermissions(WildernessPerms.ROOT);

        // Create & Register Core Services
        WildernessIOService ioService = new WildernessIOService(logger, regionsDir, ID);
        WildernessRepository repository = new WildernessRepository();
        WildernessDataService dataService = new WildernessDataService(ioService, repository);

        WildernessModule module = new WildernessModule(ID, moduleDir, repository, dataService);

        // Create Settings instance and attach SettingsController to module
        WildernessSettings settings = configureSettings(module, regionsContainer);
        MessageDispatcher messages = new StandardMessageDispatcher(settings);

        regionsContainer.register(ConfigurableModule.class, module);
        regionsContainer.register(WildernessModule.class, module);
        regionsContainer.register(WildernessRepository.class, repository);
        regionsContainer.register(WildernessDataService.class, dataService);
        regionsContainer.register(MessageDispatcher.class, messages);

        module.addComponent(new WildernessWorldController(plugin, dataService, ioService, settings));

        // Configurations

        WildernessCommandConfiguration.configure(module, regionsContainer);
        WildernessUIConfiguration.configure(module, regionsContainer);

        WildernessRulesConfiguration.configure(module, regionsContainer);
        WildernessEditorConfiguration.configure(module, regionsContainer);

        container.register(WildernessModule.class, module);

        return module;
    }

    private static WildernessSettings configureSettings(WildernessModule module, DependencyContainer container) {
        Path settingsFile = module.getModuleDir().resolve(FILE_CONFIG);
        WildernessSettings settings = new WildernessSettings();

        container.register(WildernessSettings.class, settings);

        module.addComponent(new SettingsController(settingsFile, settings));

        return settings;
    }
}
