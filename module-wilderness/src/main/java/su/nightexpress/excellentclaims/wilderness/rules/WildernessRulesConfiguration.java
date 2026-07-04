package su.nightexpress.excellentclaims.wilderness.rules;

import java.util.logging.Logger;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.wilderness.WildernessModule;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandResolver;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandSuggestions;
import su.nightexpress.excellentclaims.wilderness.data.WildernessDataService;
import su.nightexpress.excellentclaims.wilderness.rules.command.RulesCommand;
import su.nightexpress.excellentclaims.wilderness.rules.ui.WildernessRuleUIController;
import su.nightexpress.excellentclaims.wilderness.rules.ui.RegionRuleUIService;
import su.nightexpress.excellentclaims.wilderness.rules.ui.button.RulesButton;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;

public final class WildernessRulesConfiguration {

    private WildernessRulesConfiguration() {
    }

    public static void configure(WildernessModule module, DependencyContainer container) {
        Logger logger = container.get(Logger.class);

        RulesAPI rulesAPI = container.getOrNull(RulesAPI.class);
        if (rulesAPI == null) {
            logger.info("Rules module is missing, some of the Wilderness module feature will be unavailable.");
            return;
        }

        configureUI(container, rulesAPI);
        configureCommands(container);
    }

    private static void configureUI(DependencyContainer container, RulesAPI rulesAPI) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);
        WildernessUIService coreUI = container.get(WildernessUIService.class);
        WildernessDataService dataService = container.get(WildernessDataService.class);

        RegionRuleUIService uiService = new RegionRuleUIService(rulesAPI, coreUI, dataService);
        WildernessRuleUIController uiController = new WildernessRuleUIController(uiService, dispatcher);

        coreUI.registerButton(new RulesButton(uiService, uiController));

        container.register(RegionRuleUIService.class, uiService);
    }

    private static void configureCommands(DependencyContainer container) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        WildernessCommandResolver resolver = container.get(WildernessCommandResolver.class);
        WildernessCommandSuggestions suggestions = container.get(WildernessCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        RegionRuleUIService uiService = container.get(RegionRuleUIService.class);

        commands.registerCommand(new RulesCommand(resolver, suggestions, uiService, dispatcher));
    }
}
