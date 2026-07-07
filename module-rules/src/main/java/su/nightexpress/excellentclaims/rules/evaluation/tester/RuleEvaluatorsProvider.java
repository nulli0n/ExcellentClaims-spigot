package su.nightexpress.excellentclaims.rules.evaluation.tester;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.tester.EntityTester;
import su.nightexpress.excellentclaims.api.rule.tester.EnvironmentTester;
import su.nightexpress.excellentclaims.api.rule.tester.PlayerTester;
import su.nightexpress.excellentclaims.api.rule.tester.RuleEvaluators;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;

@NullMarked
public class RuleEvaluatorsProvider implements RuleEvaluators {

    private final EvaluatorEngine engine;

    private final PlayerTester      playerTester;
    private final EntityTester      entityTester;
    private final EnvironmentTester environmentTester;

    public RuleEvaluatorsProvider(EvaluatorEngine engine) {
        this.engine = engine;

        this.playerTester = new PlayerRuleTester(engine);
        this.entityTester = new EntityRuleTester(engine);
        this.environmentTester = new EnvironmentRuleTester(engine);
    }

    @Override
    public EventState evaluate(ActionContext context) {
        return this.engine.evaluate(context).state();
    }

    @Override
    public EntityTester entity() {
        return this.entityTester;
    }

    @Override
    public EnvironmentTester environment() {
        return this.environmentTester;
    }

    @Override
    public PlayerTester player() {
        return this.playerTester;
    }
}
