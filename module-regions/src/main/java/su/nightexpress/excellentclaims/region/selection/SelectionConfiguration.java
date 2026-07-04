package su.nightexpress.excellentclaims.region.selection;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.SimpleDependencies;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.claim.RegionClaimService;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.selection.command.ClaimCommand;
import su.nightexpress.excellentclaims.region.selection.command.WandCommand;
import su.nightexpress.excellentclaims.region.selection.session.SessionController;
import su.nightexpress.excellentclaims.region.selection.session.SessionManager;
import su.nightexpress.excellentclaims.region.selection.session.SessionService;
import su.nightexpress.excellentclaims.region.selection.settings.SelectionSettings;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIConfiguration;
import su.nightexpress.excellentclaims.region.selection.ui.SelectionUIService;

@NullMarked
public final class SelectionConfiguration {

    private static final Identifier ID = Identifier.of("regions_selection");

    private static final String SETTINGS_FILE = "selection.yml";

    private SelectionConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        DependencyContainer selectionContainer = new SimpleDependencies(container);

        SelectionModule selectionModule = new SelectionModule(ID);
        SessionManager sessionManager = new SessionManager();

        selectionContainer.register(SessionManager.class, sessionManager);

        SelectionSettings settings = configureSettings(selectionModule, selectionContainer);
        if (settings.isUIEnabled()) {
            SelectionUIConfiguration.configure(selectionModule, selectionContainer);
        }

        configureSelection(selectionContainer);
        configureSession(selectionModule, selectionContainer);

        module.addComponent(selectionModule);
    }

    private static SelectionSettings configureSettings(SelectionModule module, DependencyContainer container) {
        RegionsModule parent = container.get(RegionsModule.class);

        Path filePath = parent.getModuleDir().resolve(SETTINGS_FILE);

        return SettingsFactory.create(module, container, filePath, SelectionSettings::new);
    }

    private static void configureSelection(DependencyContainer container) {
        RegionQuotaValidator quotaValidator = container.get(RegionQuotaValidator.class);
        RegionClaimService claimService = container.get(RegionClaimService.class);

        SelectionService selectionService = new SelectionService(quotaValidator, claimService);

        container.register(SelectionService.class, selectionService);
    }

    private static void configureSession(SelectionModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        SelectionUIService uiService = container.getOrNull(SelectionUIService.class);
        SelectionService selectionService = container.get(SelectionService.class);

        SessionManager sessionManager = container.get(SessionManager.class);
        SessionService sessionService = new SessionService(sessionManager, selectionService, uiService);

        commands.registerCommand(new WandCommand(sessionService, dispatcher));
        commands.registerCommand(new ClaimCommand(sessionService, dispatcher));

        module.addComponent(new SessionController(plugin, sessionService, dispatcher));
    }
}
