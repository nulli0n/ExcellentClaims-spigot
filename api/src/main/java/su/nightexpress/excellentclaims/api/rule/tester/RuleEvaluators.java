package su.nightexpress.excellentclaims.api.rule.tester;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

@NullMarked
public interface RuleEvaluators {

    EventState evaluate(ActionContext context);

    PlayerTester player();

    EntityTester entity();

    EnvironmentTester environment();
}
