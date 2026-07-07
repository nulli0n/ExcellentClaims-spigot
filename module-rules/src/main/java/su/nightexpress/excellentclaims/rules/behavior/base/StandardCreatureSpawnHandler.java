package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.RuleProcessor;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.CreatureSpawnContext;

@NullMarked
public abstract class StandardCreatureSpawnHandler<T> implements RuleProcessor<CreatureSpawnContext, T> {

    @Override
    public RuleResult process(CreatureSpawnContext event, ClaimRegistry claims, RuleLookup<T> context) {
        EntityType type = event.entity().getType();
        Claim claim = claims.getPrioritizedClaim(event.location());
        T allowed = context.resolveValue(claim).orElse(null);
        if (allowed == null) return RuleResult.pass();

        if (!this.isEntityAllowed(type, allowed)) {
            return RuleResult.deny();
        }

        return RuleResult.allow();
    }

    protected abstract boolean isEntityAllowed(EntityType type, @NonNull T value);
}
