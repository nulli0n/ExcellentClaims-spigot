package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseEntityBlockChangeRule extends SimpleSpec<EntityChangeBlockEvent, Boolean> {

    public BaseEntityBlockChangeRule() {
        super(EntityChangeBlockEvent.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
    }

    @Override
    public RuleBehavior<EntityChangeBlockEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> this.shouldHandle(event, event.getEntity()))
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getBlock()))
            .trigger((event, registry, claim, rule, allowed) -> {
                return RuleResult.of(allowed);
            })
            .build();
    }

    protected abstract boolean shouldHandle(EntityChangeBlockEvent event, Entity entity);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
