package su.nightexpress.excellentclaims.api.rule.tester;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

@NullMarked
public interface RuleEvaluators {

    /**
     * Processes a generic ActionContext payload against the claim rules to determine the resulting EventState.
     * 
     * @param context
     * @return
     */
    EventState evaluate(ActionContext context);

    /**
     * Retrieves the tester component dedicated to validating actions initiated by players.
     * 
     * @return
     */
    PlayerTester player();

    /**
     * Retrieves the tester component dedicated to validating actions and behaviors of non-player entities.
     * 
     * @return
     */
    EntityTester entity();

    /**
     * Retrieves the tester component dedicated to validating natural world events and block updates.
     * 
     * @return
     */
    EnvironmentTester environment();
}
