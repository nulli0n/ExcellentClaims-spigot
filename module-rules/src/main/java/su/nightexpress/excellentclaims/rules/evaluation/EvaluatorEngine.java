package su.nightexpress.excellentclaims.rules.evaluation;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.registry.RegisteredRule;

@NullMarked
public class EvaluatorEngine {

    private final RuleRegistry       rules;
    private final ClaimRegistry      claimRegistry;
    private final ClaimPermissionAPI permissions;

    public EvaluatorEngine(RuleRegistry rules,
                           ClaimRegistry claimRegistry,
                           ClaimPermissionAPI permissions) {
        this.rules = rules;
        this.claimRegistry = claimRegistry;
        this.permissions = permissions;
    }

    public boolean quickTest(ActionContext context) {
        return this.evaluate(context).state() != EventState.DENY;
    }

    public RuleResult evaluate(ActionContext context) {
        List<RegisteredRule<?, ?>> applicableRules = this.rules.getRulesByContext(context.getClass());
        if (applicableRules == null || applicableRules.isEmpty()) return RuleResult.pass();

        for (RegisteredRule<?, ?> rule : applicableRules) {
            RuleResult result = this.handleRule(rule, context);
            if (result.state() != EventState.PASS) {
                return result; // Break on first definitive Allow/Deny
            }
        }

        return RuleResult.pass();
    }

    private <C extends ActionContext, T> RuleResult handleRule(RegisteredRule<C, T> rule, ActionContext context) {
        RuleBehavior<C, T> behavior = rule.getBehavior();
        C typedContext = behavior.getContextType().cast(context);

        // Rule don't want to handle this event, pass.
        if (!behavior.shouldHandle(typedContext)) {
            return RuleResult.pass();
        }

        // Force bypass rules where applicable.
        if (context instanceof ActorContext actorContext) {
            Player actor = actorContext.actor();
            if (actor != null && this.permissions.hasBypass(actor)) {
                return RuleResult.allow();
            }
        }

        RuleLookup<T> lookup = new RuleLookup<>() {

            @Override
            public @NonNull Optional<T> resolveValue(@Nullable Claim claim) {
                if (claim == null) return Optional.empty();

                ClaimRules rules = claim.getRules();
                if (claim.isSupportingUnsetRules() && !rules.has(rule)) {
                    return Optional.empty(); // Unset rule ignored, pass.
                }

                return Optional.of(rules.getOrDefault(rule));
            }
        };

        return behavior.process(typedContext, this.claimRegistry, lookup);
    }
}
