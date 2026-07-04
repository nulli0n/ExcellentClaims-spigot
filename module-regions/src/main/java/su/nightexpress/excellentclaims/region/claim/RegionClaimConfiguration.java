package su.nightexpress.excellentclaims.region.claim;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.claim.command.RemoveCommand;
import su.nightexpress.excellentclaims.region.claim.ui.dialog.ClaimingDialogKeys;
import su.nightexpress.excellentclaims.region.claim.ui.dialog.RegionRemoveConfirmDialog;
import su.nightexpress.excellentclaims.region.claim.validation.RegionActionValidator;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

public final class RegionClaimConfiguration {

    private RegionClaimConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        EventPublisher eventPublisher = container.get(EventPublisher.class);
        RegionSettings settings = container.get(RegionSettings.class);
        RegionDataService dataService = container.get(RegionDataService.class);
        RegionActionValidator actionValidator = container.get(RegionActionValidator.class);
        RegionBillingService billingService = container.getOrNull(RegionBillingService.class);

        RegionClaimService claimService = new RegionClaimService(eventPublisher, settings, dataService, actionValidator, billingService);

        container.register(RegionClaimService.class, claimService);

        configureCommands(container, claimService);
        configureUI(container, claimService);
    }

    private static void configureCommands(DependencyContainer container, RegionClaimService service) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        RegionCommandResolver resolver = container.get(RegionCommandResolver.class);
        RegionCommandSuggestions suggestions = container.get(RegionCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        //commands.registerCommand(new ClaimCommand(suggestions, service, messages));
        commands.registerCommand(new RemoveCommand(suggestions, resolver, service, messages));
    }

    private static void configureUI(DependencyContainer container, RegionClaimService service) {
        DialogRegistry dialogs = container.get(DialogRegistry.class);

        dialogs.register(ClaimingDialogKeys.REMOVE_CONFIRMATION, () -> new RegionRemoveConfirmDialog(service));
    }
}
