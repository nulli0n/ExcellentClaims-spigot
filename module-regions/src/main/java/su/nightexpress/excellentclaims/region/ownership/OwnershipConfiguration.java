package su.nightexpress.excellentclaims.region.ownership;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.ownership.command.TransferOwnershipCommand;
import su.nightexpress.excellentclaims.region.ownership.ui.OwnershipUIContextFactory;
import su.nightexpress.excellentclaims.region.ownership.ui.OwnershipUIController;
import su.nightexpress.excellentclaims.region.ownership.ui.OwnershipUIService;
import su.nightexpress.excellentclaims.region.ownership.ui.button.TransferOwnershipButton;
import su.nightexpress.excellentclaims.region.ownership.ui.dialog.OwnershipDialogKeys;
import su.nightexpress.excellentclaims.region.ownership.ui.dialog.TransferSelectTargetDialog;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.userdata.UserDataManager;

public final class OwnershipConfiguration {

    private OwnershipConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        RegionDataService dataService = container.get(RegionDataService.class);
        RegionQuotaValidator quotaValidator = container.get(RegionQuotaValidator.class);
        ClaimPermissionAPI permissionService = container.get(ClaimPermissionAPI.class);
        RanksAPI ranksAPI = container.get(RanksAPI.class);

        plugin.injectLang(OwnershipLang.class);

        OwnershipService ownershipService = new OwnershipService(dataService, quotaValidator, permissionService, ranksAPI);

        configureUI(container, ownershipService);
        configureCommands(container, ownershipService);

        container.register(OwnershipService.class, ownershipService);
    }

    private static void configureCommands(DependencyContainer container, OwnershipService service) {
        CommandRegistry commands = container.get(CommandRegistry.class);

        RegionCommandResolver resolver = container.get(RegionCommandResolver.class);
        RegionCommandSuggestions suggestions = container.get(RegionCommandSuggestions.class);
        MessageDispatcher messages = container.get(MessageDispatcher.class);

        commands.registerCommand(new TransferOwnershipCommand(resolver, suggestions, service, messages));
    }

    private static void configureUI(DependencyContainer container, OwnershipService service) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        UserDataManager users = container.get(UserDataManager.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RegionUIService coreUI = container.get(RegionUIService.class);

        OwnershipUIService uiService = new OwnershipUIService(dialogs, service, coreUI);
        OwnershipUIContextFactory uiContextFactory = new OwnershipUIContextFactory(plugin, users, service);
        OwnershipUIController uiController = new OwnershipUIController(service, uiService, uiContextFactory, dispatcher);

        coreUI.registerButton(new TransferOwnershipButton(uiController, service));

        dialogs.register(OwnershipDialogKeys.TRANSFER_SELECT_TARGET, new TransferSelectTargetDialog(uiController));
    }
}
