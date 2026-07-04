package su.nightexpress.excellentclaims.rules.ui;

import java.nio.file.Path;
import java.util.logging.Logger;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsConstants;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.RulesModule;
import su.nightexpress.excellentclaims.rules.editor.EditorRegistry;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleListMenu;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleMenuLoader;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleMenus;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

public final class RuleUIConfiguration {

    private RuleUIConfiguration() {
    }

    public static void configure(RulesModule module, DependencyContainer container) {
        configureUI(module, container);
    }

    private static void configureUI(RulesModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        DialogRegistry dialogs = container.get(DialogRegistry.class);
        Logger logger = container.get(Logger.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);

        Path menuDir = plugin.dataPath().resolve(ClaimsConstants.DIR_MENU);

        EditorRegistry editors = RuleUIRegistrar.createAndPopulateEditors(dialogs);

        RuleRegistry rules = container.get(RuleRegistry.class);
        RuleUIService uiService = new RuleUIService(permissions);
        RuleUIController uiController = new RuleUIController(logger, editors, uiService);

        RuleListMenu rulesMenu = new RuleListMenu(plugin, rules, uiController);
        RuleMenus menus = new RuleMenus(rulesMenu);
        RuleMenuLoader menuLoader = new RuleMenuLoader(menuDir, menus);

        container.register(RuleUIService.class, uiService);

        uiService.registerMenus(menus);

        module.addComponent(menuLoader);
    }
}
