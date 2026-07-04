package su.nightexpress.excellentclaims.rules.ui.dialog;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;

@NullMarked
public record RuleDialogContext<T>(Claim claim, ClaimRule<T> rule, Runnable dirtyFlag) {

}
