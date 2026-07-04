package su.nightexpress.excellentclaims.land.rules;

import java.util.logging.Logger;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.rules.command.RulesCommand;
import su.nightexpress.excellentclaims.land.rules.ui.LandRuleUIController;
import su.nightexpress.excellentclaims.land.rules.ui.LandRuleUIService;
import su.nightexpress.excellentclaims.land.rules.ui.button.RulesButton;
import su.nightexpress.excellentclaims.land.ui.LandUIService;

public final class LandRulesConfiguration {

    private LandRulesConfiguration() {
    }

    public static void configure(LandsModule module, DependencyContainer container) {
        Logger logger = container.get(Logger.class);

        RulesAPI rulesAPI = container.getOrNull(RulesAPI.class);
        if (rulesAPI == null) {
            logger.info("Rules module is missing, some of the Lands module feature will be unavailable.");
            return;
        }

        configureUI(container, rulesAPI);
        configureCommands(container);
    }

    private static void configureUI(DependencyContainer container, RulesAPI rulesAPI) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);
        LandUIService coreUI = container.get(LandUIService.class);
        LandDataService dataService = container.get(LandDataService.class);

        LandRuleUIService uiService = new LandRuleUIService(rulesAPI, coreUI, dataService);
        LandRuleUIController uiController = new LandRuleUIController(uiService, dispatcher);

        coreUI.registerButton(new RulesButton(uiService, uiController));

        container.register(LandRuleUIService.class, uiService);
    }

    private static void configureCommands(DependencyContainer container) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        LandCommandResolver resolver = container.get(LandCommandResolver.class);
        LandCommandSuggestions suggestions = container.get(LandCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        LandRuleUIService uiService = container.get(LandRuleUIService.class);

        commands.registerCommand(new RulesCommand(resolver, suggestions, uiService, dispatcher));
    }
}
