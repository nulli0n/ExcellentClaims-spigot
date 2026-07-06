package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleContext;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.RuleProcessor;

@NullMarked
public abstract class StandardCreatureSpawnHandler<T> implements RuleProcessor<CreatureSpawnEvent, T> {

    @Override
    public RuleResult process(CreatureSpawnEvent event, ClaimRegistry claims, RuleContext<T> context) {
        EntityType type = event.getEntity().getType();
        Claim claim = claims.getPrioritizedClaim(event.getLocation());
        T allowed = context.resolveValue(claim).orElse(null);

        if (!this.isEntityAllowed(type, allowed)) {
            return RuleResult.deny();
        }

        return RuleResult.allow();
    }

    protected abstract boolean isEntityAllowed(EntityType type, @NonNull T value);
}
