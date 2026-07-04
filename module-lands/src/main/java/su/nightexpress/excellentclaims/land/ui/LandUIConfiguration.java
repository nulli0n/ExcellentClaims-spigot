package su.nightexpress.excellentclaims.land.ui;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.ui.command.InspectCommand;
import su.nightexpress.excellentclaims.land.ui.command.ListCommand;
import su.nightexpress.excellentclaims.land.ui.command.MenuCommand;
import su.nightexpress.excellentclaims.land.ui.menu.LandClaimMenu;
import su.nightexpress.excellentclaims.land.ui.menu.LandInspectMenu;
import su.nightexpress.excellentclaims.land.ui.menu.LandListMenu;
import su.nightexpress.excellentclaims.land.ui.menu.LandMenus;
import su.nightexpress.nightcore.userdata.UserDataManager;

public final class LandUIConfiguration {

    private LandUIConfiguration() {
    }

    public static void configure(LandsModule module, DependencyContainer container) {
        configureUI(container, module);
        configureCommands(container);
    }

    private static void configureUI(DependencyContainer container, LandsModule module) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        //DialogRegistry dialogs = container.get(DialogRegistry.class);
        UserDataManager users = container.get(UserDataManager.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RanksAPI ranksAPI = container.getOrNull(RanksAPI.class);

        LandsRepository repository = container.get(LandsRepository.class);
        LandUIService uiService = new LandUIService(permissions);
        LandUIContextFactory uiContextFactory = new LandUIContextFactory(plugin, users, repository);
        LandUIController uiController = new LandUIController(uiService, uiContextFactory, dispatcher);

        Path menuDir = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        LandListMenu listMenu = new LandListMenu(plugin, repository, uiController, ranksAPI);
        LandInspectMenu inspectMenu = new LandInspectMenu(plugin, uiController, permissions, ranksAPI);
        LandClaimMenu settingsMenu = new LandClaimMenu(plugin, uiService);

        LandMenus menus = new LandMenus(listMenu, inspectMenu, settingsMenu);
        LandUILoader loader = new LandUILoader(menuDir, menus);

        uiService.registerMenus(menus);

        container.register(LandUIService.class, uiService);
        container.register(LandUIContextFactory.class, uiContextFactory);

        module.addComponent(loader);
    }

    private static void configureCommands(DependencyContainer container) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        LandUIService ui = container.get(LandUIService.class);
        LandUIContextFactory contextFactory = container.get(LandUIContextFactory.class);

        LandCommandResolver resolver = container.get(LandCommandResolver.class);
        LandCommandSuggestions suggestions = container.get(LandCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        commands.registerCommand(new MenuCommand(resolver, suggestions, ui, dispatcher));
        commands.registerCommand(new ListCommand(ui, dispatcher));
        commands.registerCommand(new InspectCommand(ui, contextFactory, dispatcher));
    }
}
