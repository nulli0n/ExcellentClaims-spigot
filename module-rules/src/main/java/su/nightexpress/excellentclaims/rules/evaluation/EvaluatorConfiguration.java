package su.nightexpress.excellentclaims.rules.evaluation;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.RulesModule;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

public final class EvaluatorConfiguration {

    private EvaluatorConfiguration() {
    }

    public static void configure(RulesModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimRegistry claims = container.get(ClaimRegistry.class);
        RuleRegistry rules = container.get(RuleRegistry.class);
        ClaimPermissionAPI permissions = container.get(ClaimPermissionAPI.class);
        MessageDispatcher dispatcher = container.get(MessageDispatcher.class);

        RulesSettings settings = container.get(RulesSettings.class);

        EvaluatorEngine engine = new EvaluatorEngine(rules, claims, permissions);
        EventControllerService controllerService = new EventControllerService(engine, settings, dispatcher);
        EventRegisterController registerController = new EventRegisterController(plugin, controllerService);

        container.register(EvaluatorEngine.class, engine);

        module.addComponent(registerController);
    }
}
