package su.nightexpress.excellentclaims.land.ownership;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.land.claim.validation.LandQuotaValidator;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.ownership.command.TransferOwnershipCommand;
import su.nightexpress.excellentclaims.land.ownership.ui.OwnershipUIContextFactory;
import su.nightexpress.excellentclaims.land.ownership.ui.OwnershipUIController;
import su.nightexpress.excellentclaims.land.ownership.ui.OwnershipUIService;
import su.nightexpress.excellentclaims.land.ownership.ui.button.TransferOwnershipButton;
import su.nightexpress.excellentclaims.land.ownership.ui.dialog.OwnershipDialogKeys;
import su.nightexpress.excellentclaims.land.ownership.ui.dialog.TransferSelectTargetDialog;
import su.nightexpress.excellentclaims.land.ui.LandUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.userdata.UserDataManager;

public final class LandOwnershipConfiguration {

    private LandOwnershipConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        LandDataService dataService = container.get(LandDataService.class);
        LandQuotaValidator quotaValidator = container.get(LandQuotaValidator.class);
        ClaimPermissionAPI permissionService = container.get(ClaimPermissionAPI.class);
        RanksAPI ranksAPI = container.get(RanksAPI.class);

        plugin.injectLang(OwnershipLang.class);

        LandOwnershipService ownershipService = new LandOwnershipService(dataService, quotaValidator, permissionService, ranksAPI);

        configureUI(container, ownershipService);
        configureCommands(container, ownershipService);

        container.register(LandOwnershipService.class, ownershipService);
    }

    private static void configureCommands(DependencyContainer container, LandOwnershipService service) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        LandCommandResolver resolver = container.get(LandCommandResolver.class);
        LandCommandSuggestions suggestions = container.get(LandCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        commands.registerCommand(new TransferOwnershipCommand(resolver, suggestions, service, messages));
    }

    private static void configureUI(DependencyContainer container, LandOwnershipService service) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        UserDataManager users = container.get(UserDataManager.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        LandUIService coreUI = container.get(LandUIService.class);

        OwnershipUIService uiService = new OwnershipUIService(dialogs, service, coreUI);
        OwnershipUIContextFactory uiContextFactory = new OwnershipUIContextFactory(plugin, users, service);
        OwnershipUIController uiController = new OwnershipUIController(service, uiService, uiContextFactory, dispatcher);

        coreUI.registerButton(new TransferOwnershipButton(uiController, service));

        dialogs.register(OwnershipDialogKeys.TRANSFER_SELECT_TARGET, new TransferSelectTargetDialog(uiController));
    }
}
