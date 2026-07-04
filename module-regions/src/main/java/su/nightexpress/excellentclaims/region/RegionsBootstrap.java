package su.nightexpress.excellentclaims.region;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModule;
import su.nightexpress.excellentclaims.api.claim.module.ClaimModuleBootstrap;
import su.nightexpress.excellentclaims.api.core.ConfigurableModule;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.core.settings.SettingsController;
import su.nightexpress.excellentclaims.core.StandardMessageDispatcher;
import su.nightexpress.excellentclaims.region.claim.RegionBillingService;
import su.nightexpress.excellentclaims.region.claim.RegionClaimConfiguration;
import su.nightexpress.excellentclaims.region.claim.validation.RegionActionValidator;
import su.nightexpress.excellentclaims.region.claim.validation.RegionOverlapValidator;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.command.RegionCommandConfiguration;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.editor.RegionEditorConfiguration;
import su.nightexpress.excellentclaims.region.io.RegionIOService;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.member.MemberConfiguration;
import su.nightexpress.excellentclaims.region.ownership.OwnershipConfiguration;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.rules.RegionRulesConfiguration;
import su.nightexpress.excellentclaims.region.selection.SelectionConfiguration;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.excellentclaims.region.ui.RegionUIConfiguration;

@NullMarked
public class RegionsBootstrap implements ClaimModuleBootstrap {

    private static final Identifier ID = Identifier.of("regions");

    private static final String FILE_CONFIG = "regions.yml";
    private static final String DIR_REGIONS = "regions";
    private static final String DIR_CONFIG  = "regions";

    @Override
    public String getId() {
        return "regions";
    }

    @Override
    public ClaimModule bootstrap(DependencyContainer container) {
        DependencyContainer regionsContainer = new SimpleDependencies(container);

        //EventPublisher eventPublisher = container.get(EventPublisher.class);
        //UserDataManager users = container.get(UserDataManager.class);
        Logger logger = container.get(Logger.class);
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        ClaimRegistry claims = container.get(ClaimRegistry.class);
        ClaimPermissionAPI permissionService = container.get(ClaimPermissionAPI.class);

        Path regionsDir = plugin.dataPath().resolve(DIR_REGIONS);
        Path moduleDir = plugin.dataPath().resolve(ClaimsConstants.DIR_CONFIG).resolve(DIR_CONFIG);

        // Register Core Codecs
        RegionsCodecsRegistrar.register();

        // Inject Lang & Permission before service configuration
        plugin.injectLang(RegionLang.class);
        plugin.registerPermissions(RegionPerms.ROOT);

        // Create & Register Core Services
        RegionIOService ioService = new RegionIOService(logger, regionsDir, ID);
        RegionsRepository repository = new RegionsRepository();
        RegionDataService dataService = new RegionDataService(ioService, repository);

        RegionsModule module = new RegionsModule(ID, moduleDir, repository, dataService);

        // Create Settings instance and attach SettingsController to module
        RegionSettings settings = configureSettings(module, regionsContainer);
        MessageDispatcher messages = new StandardMessageDispatcher(settings);

        regionsContainer.register(ConfigurableModule.class, module);
        regionsContainer.register(RegionsModule.class, module);
        regionsContainer.register(RegionsRepository.class, repository);
        regionsContainer.register(RegionDataService.class, dataService);
        regionsContainer.register(MessageDispatcher.class, messages);

        // Configurations

        RegionCommandConfiguration.configure(module, regionsContainer);
        RegionUIConfiguration.configure(module, regionsContainer);

        RegionQuotaValidator quotaValidator = new RegionQuotaValidator(settings, repository);
        RegionOverlapValidator overlapValidator = new RegionOverlapValidator(claims, repository, settings);
        RegionActionValidator actionValidator = new RegionActionValidator(settings, permissionService, quotaValidator, overlapValidator);

        regionsContainer.register(RegionQuotaValidator.class, quotaValidator);
        regionsContainer.register(RegionOverlapValidator.class, overlapValidator);
        regionsContainer.register(RegionActionValidator.class, actionValidator);

        if (settings.isBillingEnabled()) {
            RegionBillingService billingService = new RegionBillingService(settings);
            regionsContainer.register(RegionBillingService.class, billingService);
        }

        RegionRulesConfiguration.configure(module, regionsContainer);
        OwnershipConfiguration.configure(regionsContainer);
        RegionEditorConfiguration.configure(module, regionsContainer);
        RegionClaimConfiguration.configure(module, regionsContainer);

        MemberConfiguration.configure(module, regionsContainer);
        //BordersConfiguration.configure(module, regionsContainer);
        SelectionConfiguration.configure(module, regionsContainer);

        container.register(RegionsModule.class, module);

        return module;
    }

    private static RegionSettings configureSettings(RegionsModule module, DependencyContainer container) {
        Path settingsFile = module.getModuleDir().resolve(FILE_CONFIG);
        RegionSettings settings = new RegionSettings();

        container.register(RegionSettings.class, settings);

        module.addComponent(new SettingsController(settingsFile, settings));

        return settings;
    }
}
