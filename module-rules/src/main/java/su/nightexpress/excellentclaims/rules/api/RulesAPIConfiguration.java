package su.nightexpress.excellentclaims.rules.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.APIContainer;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.tester.RuleEvaluatorsProvider;
import su.nightexpress.excellentclaims.rules.ui.RuleUIService;

@NullMarked
public final class RulesAPIConfiguration {

    private RulesAPIConfiguration() {
    }

    public static RulesAPI configure(DependencyContainer container) {
        APIContainer apiContainer = container.get(APIContainer.class);
        RuleRegistry rules = container.get(RuleRegistry.class);
        RuleUIService uiService = container.get(RuleUIService.class);
        EvaluatorEngine engine = container.get(EvaluatorEngine.class);

        RuleEvaluatorsProvider evaluators = new RuleEvaluatorsProvider(engine);

        RulesAPI api = new RulesAPIProvider(rules, uiService, evaluators);

        apiContainer.container().register(RulesAPI.class, api);

        return api;
    }
}
