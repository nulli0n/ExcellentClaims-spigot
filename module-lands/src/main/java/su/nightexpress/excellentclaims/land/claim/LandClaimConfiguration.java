package su.nightexpress.excellentclaims.land.claim;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.claim.command.ClaimCommand;
import su.nightexpress.excellentclaims.land.claim.command.UnclaimCommand;
import su.nightexpress.excellentclaims.land.claim.ui.dialog.ClaimingDialogKeys;
import su.nightexpress.excellentclaims.land.claim.ui.dialog.LandRemoveConfirmDialog;
import su.nightexpress.excellentclaims.land.claim.validation.LandActionValidator;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

public final class LandClaimConfiguration {

    private LandClaimConfiguration() {
    }

    public static void configure(LandsModule module, DependencyContainer container) {
        EventPublisher eventPublisher = container.get(EventPublisher.class);
        LandSettings settings = container.get(LandSettings.class);
        LandDataService dataService = container.get(LandDataService.class);
        LandActionValidator actionValidator = container.get(LandActionValidator.class);
        LandBillingService billingService = container.getOrNull(LandBillingService.class);

        LandClaimService claimService = new LandClaimService(eventPublisher, settings, dataService, actionValidator, billingService);

        container.register(LandClaimService.class, claimService);

        configureCommands(container, claimService);
        configureUI(container, claimService);
    }

    private static void configureCommands(DependencyContainer container, LandClaimService service) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        LandCommandResolver resolver = container.get(LandCommandResolver.class);
        LandCommandSuggestions suggestions = container.get(LandCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        commands.registerCommand(new ClaimCommand(suggestions, service, messages));
        commands.registerCommand(new UnclaimCommand(suggestions, resolver, service, messages));
    }

    private static void configureUI(DependencyContainer container, LandClaimService service) {
        DialogRegistry dialogs = container.get(DialogRegistry.class);

        dialogs.register(ClaimingDialogKeys.REMOVE_CONFIRMATION, () -> new LandRemoveConfirmDialog(service));
    }
}
