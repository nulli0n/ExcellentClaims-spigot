package su.nightexpress.excellentclaims.wilderness.ui;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.wilderness.WildernessModule;
import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandResolver;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandSuggestions;
import su.nightexpress.excellentclaims.wilderness.ui.command.ListCommand;
import su.nightexpress.excellentclaims.wilderness.ui.command.MenuCommand;
import su.nightexpress.excellentclaims.wilderness.ui.menu.WildernessListMenu;
import su.nightexpress.excellentclaims.wilderness.ui.menu.WildernessMainMenu;
import su.nightexpress.excellentclaims.wilderness.ui.menu.WildernessMenus;

@NullMarked
public final class WildernessUIConfiguration {

    private WildernessUIConfiguration() {
    }

    public static void configure(WildernessModule module, DependencyContainer container) {
        configureUI(container, module);
        configureCommands(container);
    }

    private static void configureUI(DependencyContainer container, WildernessModule module) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        //DialogRegistry dialogs = container.get(DialogRegistry.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        WildernessRepository repository = container.get(WildernessRepository.class);
        WildernessUIService uiService = new WildernessUIService(permissions);
        WildernessUIController uiController = new WildernessUIController(uiService, dispatcher);

        Path menuDir = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        WildernessListMenu listMenu = new WildernessListMenu(plugin, repository, uiController);
        WildernessMainMenu settingsMenu = new WildernessMainMenu(plugin, uiService);

        WildernessMenus menus = new WildernessMenus(listMenu, settingsMenu);
        WildernessUILoader loader = new WildernessUILoader(menuDir, menus);

        uiService.registerMenus(menus);

        container.register(WildernessUIService.class, uiService);

        module.addComponent(loader);
    }

    private static void configureCommands(DependencyContainer container) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        WildernessUIService ui = container.get(WildernessUIService.class);

        WildernessCommandResolver resolver = container.get(WildernessCommandResolver.class);
        WildernessCommandSuggestions suggestions = container.get(WildernessCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        commands.registerCommand(new MenuCommand(resolver, suggestions, ui, dispatcher));
        commands.registerCommand(new ListCommand(ui, dispatcher));
    }
}
