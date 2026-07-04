package su.nightexpress.excellentclaims.api.rule;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.service.ActionResult;

@NullMarked
public record RuleResult(EventState state, @Nullable ActionResult feedback) {

    public static RuleResult of(boolean result) {
        return new RuleResult(result ? EventState.ALLOW : EventState.DENY, null);
    }

    public static RuleResult of(boolean result, ActionResult feedback) {
        return new RuleResult(result ? EventState.ALLOW : EventState.DENY, feedback);
    }

    public static RuleResult pass() {
        return new RuleResult(EventState.PASS, null);
    }

    public static RuleResult allow() {
        return new RuleResult(EventState.ALLOW, null);
    }

    public static RuleResult allow(ActionResult feedback) {
        return new RuleResult(EventState.ALLOW, feedback);
    }

    public static RuleResult deny(ActionResult feedback) {
        return new RuleResult(EventState.DENY, feedback);
    }

    public static RuleResult deny() {
        return new RuleResult(EventState.DENY, null);
    }
}