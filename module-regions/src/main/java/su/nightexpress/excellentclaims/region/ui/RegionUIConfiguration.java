package su.nightexpress.excellentclaims.region.ui;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.ui.command.InspectCommand;
import su.nightexpress.excellentclaims.region.ui.command.ListCommand;
import su.nightexpress.excellentclaims.region.ui.command.MenuCommand;
import su.nightexpress.excellentclaims.region.ui.menu.RegionMainMenu;
import su.nightexpress.excellentclaims.region.ui.menu.RegionInspectMenu;
import su.nightexpress.excellentclaims.region.ui.menu.RegionListMenu;
import su.nightexpress.excellentclaims.region.ui.menu.RegionMenus;
import su.nightexpress.nightcore.userdata.UserDataManager;

public final class RegionUIConfiguration {

    private RegionUIConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        configureUI(container, module);
        configureCommands(container);
    }

    private static void configureUI(DependencyContainer container, RegionsModule module) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        //DialogRegistry dialogs = container.get(DialogRegistry.class);
        UserDataManager users = container.get(UserDataManager.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RanksAPI ranksAPI = container.getOrNull(RanksAPI.class);

        RegionsRepository repository = container.get(RegionsRepository.class);
        RegionUIService uiService = new RegionUIService(permissions);
        RegionUIContextFactory uiContextFactory = new RegionUIContextFactory(plugin, users, repository);
        RegionUIController uiController = new RegionUIController(uiService, uiContextFactory, dispatcher);

        Path menuDir = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        RegionListMenu listMenu = new RegionListMenu(plugin, repository, uiController, ranksAPI);
        RegionInspectMenu inspectMenu = new RegionInspectMenu(plugin, uiController, permissions, ranksAPI);
        RegionMainMenu settingsMenu = new RegionMainMenu(plugin, uiService);

        RegionMenus menus = new RegionMenus(listMenu, inspectMenu, settingsMenu);
        RegionUILoader loader = new RegionUILoader(menuDir, menus);

        uiService.registerMenus(menus);

        container.register(RegionUIService.class, uiService);
        container.register(RegionUIContextFactory.class, uiContextFactory);

        module.addComponent(loader);
    }

    private static void configureCommands(DependencyContainer container) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RegionUIService ui = container.get(RegionUIService.class);
        RegionUIContextFactory contextFactory = container.get(RegionUIContextFactory.class);

        RegionCommandResolver resolver = container.get(RegionCommandResolver.class);
        RegionCommandSuggestions suggestions = container.get(RegionCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        commands.registerCommand(new MenuCommand(resolver, suggestions, ui, dispatcher));
        commands.registerCommand(new ListCommand(ui, dispatcher));
        commands.registerCommand(new InspectCommand(ui, contextFactory, dispatcher));
    }
}
