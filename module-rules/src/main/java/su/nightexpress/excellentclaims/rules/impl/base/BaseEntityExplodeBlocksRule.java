package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseEntityExplodeBlocksRule extends SimpleSpec<EntityChangeBlockContext, Boolean> {

    public BaseEntityExplodeBlocksRule() {
        super(EntityChangeBlockContext.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
    }

    @Override
    public RuleBehavior<EntityChangeBlockContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.shouldHandle(context, context.entity()))
            .process((context, registry, resolver) -> {
                Claim claim = registry.getPrioritizedClaim(context.block());
                Optional<Boolean> state = resolver.resolveValue(claim);
                if (state.isPresent() && !state.get()) return RuleResult.deny();

                return RuleResult.pass();
            })
            .build();
    }

    protected abstract boolean shouldHandle(EntityChangeBlockContext context, Entity entity);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
