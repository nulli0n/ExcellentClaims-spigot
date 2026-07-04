package su.nightexpress.excellentclaims.region.rules;

import java.util.logging.Logger;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandRegistry;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.region.RegionsModule;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.rules.command.RulesCommand;
import su.nightexpress.excellentclaims.region.rules.ui.RegionRuleUIController;
import su.nightexpress.excellentclaims.region.rules.ui.RegionRuleUIService;
import su.nightexpress.excellentclaims.region.rules.ui.button.RulesButton;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;

public final class RegionRulesConfiguration {

    private RegionRulesConfiguration() {
    }

    public static void configure(RegionsModule module, DependencyContainer container) {
        Logger logger = container.get(Logger.class);

        RulesAPI rulesAPI = container.getOrNull(RulesAPI.class);
        if (rulesAPI == null) {
            logger.info("Rules module is missing, some of the Regions module feature will be unavailable.");
            return;
        }

        configureUI(container, rulesAPI);
        configureCommands(container);
    }

    private static void configureUI(DependencyContainer container, RulesAPI rulesAPI) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);
        RegionUIService coreUI = container.get(RegionUIService.class);
        RegionDataService dataService = container.get(RegionDataService.class);

        RegionRuleUIService uiService = new RegionRuleUIService(rulesAPI, coreUI, dataService);
        RegionRuleUIController uiController = new RegionRuleUIController(uiService, dispatcher);

        coreUI.registerButton(new RulesButton(uiService, uiController));

        container.register(RegionRuleUIService.class, uiService);
    }

    private static void configureCommands(DependencyContainer container) {
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RegionCommandResolver resolver = container.get(RegionCommandResolver.class);
        RegionCommandSuggestions suggestions = container.get(RegionCommandSuggestions.class);
        CommandRegistry commands = container.get(CommandRegistry.class);

        RegionRuleUIService uiService = container.get(RegionRuleUIService.class);

        commands.registerCommand(new RulesCommand(resolver, suggestions, uiService, dispatcher));
    }
}
