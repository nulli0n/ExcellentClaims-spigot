package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.HandleTrigger;

@NullMarked
public abstract class StandardCreatureSpawnHandler<T> implements HandleTrigger<CreatureSpawnEvent, T> {

    @Override
    public RuleResult handle(CreatureSpawnEvent event, ClaimRegistry registry, Claim claim, ClaimRule<T> rule,
                             @NonNull T value) {

        EntityType type = event.getEntity().getType();

        if (!this.isEntityAllowed(type, value)) {
            return RuleResult.deny();
        }

        return RuleResult.allow();
    }

    protected abstract boolean isEntityAllowed(EntityType type, @NonNull T value);
}
