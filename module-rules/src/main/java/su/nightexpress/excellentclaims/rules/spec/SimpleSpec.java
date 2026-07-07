package su.nightexpress.excellentclaims.rules.spec;

import java.util.Optional;

import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior;

@NullMarked
public abstract class SimpleSpec<E extends ActionContext, T> extends AbstractSpec<E, T> {

    public SimpleSpec(Class<E> eventType, RuleType<T> type, RuleCategory category) {
        super(eventType, type, category);
    }

    public AbstractBehavior.Builder<E, T> behaviorBuilder() {
        return new AbstractBehavior.Builder<>(this.eventType);
    }

    /**
     * Evaluates a sequence of blocks against the claim registry and rule context.
     * Returns true immediately if ANY of the evaluated blocks explicitly deny the rule.
     */
    protected boolean isAnyBlockDenied(ClaimRegistry registry,
                                       RuleLookup<Boolean> context,
                                       @Nullable Block... blocks) {
        for (Block block : blocks) {
            if (block == null) continue;

            Claim claim = registry.getPrioritizedClaim(block);
            Optional<Boolean> state = context.resolveValue(claim);

            if (state.isPresent() && !state.get()) {
                return true;
            }
        }

        return false;
    }
}
