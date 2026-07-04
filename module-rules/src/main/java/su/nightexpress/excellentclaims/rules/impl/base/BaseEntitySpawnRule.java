package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseEntitySpawnRule extends SimpleSpec<CreatureSpawnEvent, Boolean> {

    public BaseEntitySpawnRule() {
        super(CreatureSpawnEvent.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
    }

    @Override
    public RuleBehavior<CreatureSpawnEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .shouldHandle(event -> this.shouldHandle(event, event.getEntity()))
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getLocation()))
            .trigger((event, registry, claim, rule, allowed) -> {
                return RuleResult.of(allowed);
            })
            .build();
    }

    protected abstract boolean shouldHandle(CreatureSpawnEvent event, LivingEntity entity);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
