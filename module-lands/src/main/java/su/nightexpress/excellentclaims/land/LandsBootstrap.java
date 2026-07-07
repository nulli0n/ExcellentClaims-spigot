package su.nightexpress.excellentclaims.land;

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
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.BuildConstants;
import su.nightexpress.excellentclaims.core.StandardMessageDispatcher;
import su.nightexpress.excellentclaims.land.borders.BordersConfiguration;
import su.nightexpress.excellentclaims.land.claim.LandBillingService;
import su.nightexpress.excellentclaims.land.claim.LandClaimConfiguration;
import su.nightexpress.excellentclaims.land.claim.validation.LandActionValidator;
import su.nightexpress.excellentclaims.land.claim.validation.LandOverlapValidator;
import su.nightexpress.excellentclaims.land.claim.validation.LandQuotaValidator;
import su.nightexpress.excellentclaims.land.command.LandCommandConfiguration;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.editor.LandEditorConfiguration;
import su.nightexpress.excellentclaims.land.io.LandIOService;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.member.MemberConfiguration;
import su.nightexpress.excellentclaims.land.ownership.LandOwnershipConfiguration;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.placeholders.LandPlaceholdersConfiguration;
import su.nightexpress.excellentclaims.land.rules.LandRulesConfiguration;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.excellentclaims.land.settings.LandSettingsConfiguration;
import su.nightexpress.excellentclaims.land.ui.LandUIConfiguration;

@NullMarked
public class LandsBootstrap implements ClaimModuleBootstrap {

    private static final Identifier ID = Identifier.of("lands");

    private static final String DIR_LANDS  = "lands";
    private static final String DIR_CONFIG = "lands";

    @Override
    public String getId() {
        return "lands";
    }

    @Override
    public ClaimModule bootstrap(DependencyContainer container) {
        DependencyContainer landsContainer = new SimpleDependencies(container);

        //EventPublisher eventPublisher = container.get(EventPublisher.class);
        //UserDataManager users = container.get(UserDataManager.class);
        Logger logger = container.get(Logger.class);
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        ClaimRegistry claims = container.get(ClaimRegistry.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);

        Path landsDir = plugin.dataPath().resolve(DIR_LANDS);
        Path moduleDir = plugin.dataPath().resolve(ClaimsConstants.DIR_CONFIG).resolve(DIR_CONFIG);

        // Register codecs for core configuration models
        LandsCodecsRegistrar.register();

        // Inject Lang & Permission before service configuration
        plugin.injectLang(LandLang.class);
        plugin.registerPermissions(LandPerms.ROOT);

        // Create & Register Core Services
        LandIOService ioService = new LandIOService(logger, landsDir, ID);
        LandsRepository repository = new LandsRepository();
        LandDataService dataService = new LandDataService(ioService, repository);

        LandsModule module = new LandsModule(ID, moduleDir, repository, dataService);

        // Create Settings instance and attach SettingsController to module
        LandSettings settings = LandSettingsConfiguration.configure(module, landsContainer);
        MessageDispatcher messages = new StandardMessageDispatcher(settings);

        landsContainer.register(LandsModule.class, module);
        landsContainer.register(LandsRepository.class, repository);
        landsContainer.register(LandDataService.class, dataService);
        landsContainer.register(MessageDispatcher.class, messages);

        // Configure Commands Base
        LandCommandConfiguration.configure(module, landsContainer);

        // Configure UI Base
        LandUIConfiguration.configure(module, landsContainer);

        LandQuotaValidator quotaValidator = new LandQuotaValidator(settings, repository);
        LandOverlapValidator overlapValidator = new LandOverlapValidator(claims, permissions, repository, settings);
        LandActionValidator actionValidator = new LandActionValidator(settings, permissions, quotaValidator, overlapValidator);

        landsContainer.register(LandQuotaValidator.class, quotaValidator);
        landsContainer.register(LandOverlapValidator.class, overlapValidator);
        landsContainer.register(LandActionValidator.class, actionValidator);

        if (settings.isBillingEnabled()) {
            LandBillingService billingService = new LandBillingService(settings);
            landsContainer.register(LandBillingService.class, billingService);
        }

        LandRulesConfiguration.configure(module, landsContainer);
        LandOwnershipConfiguration.configure(landsContainer);
        LandEditorConfiguration.configure(module, landsContainer);
        LandClaimConfiguration.configure(module, landsContainer);

        MemberConfiguration.configure(module, landsContainer);
        //MergeConfiguration.configure(module, landsContainer);  // TODO If enabled
        BordersConfiguration.configure(module, landsContainer); // TODO If enabled

        if (!BuildConstants.IS_LITE) {
            LandPlaceholdersConfiguration.configure(landsContainer);
        }

        container.register(LandsModule.class, module);

        return module;
    }
}
