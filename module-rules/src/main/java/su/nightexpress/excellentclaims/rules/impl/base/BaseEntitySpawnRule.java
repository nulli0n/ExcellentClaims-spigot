package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.CreatureSpawnContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseEntitySpawnRule extends SimpleSpec<CreatureSpawnContext, Boolean> {

    public BaseEntitySpawnRule() {
        super(CreatureSpawnContext.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
    }

    @Override
    public RuleBehavior<CreatureSpawnContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.shouldHandle(context, context.entity()))
            .process((context, registry, resolver) -> {
                Claim claim = registry.getPrioritizedClaim(context.location());
                Optional<Boolean> state = resolver.resolveValue(claim);
                return state.isEmpty() ? RuleResult.allow() : RuleResult.of(state.get());
            })
            .build();
    }

    protected abstract boolean shouldHandle(CreatureSpawnContext context, LivingEntity entity);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
