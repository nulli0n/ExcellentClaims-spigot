package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.Projectile;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.ProjectileLaunchContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerProjectileLaunchRule extends SimpleSpec<ProjectileLaunchContext, Boolean> {

    public BasePlayerProjectileLaunchRule() {
        super(ProjectileLaunchContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
    }

    @Override
    public RuleBehavior<ProjectileLaunchContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.shouldHandle(context, context.projectile()))
            .process((context, registry, resolver) -> {
                Projectile projectile = context.projectile();

                Claim claim = registry.getPrioritizedClaim(projectile.getLocation());
                Optional<Boolean> state = resolver.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(ProjectileLaunchContext context, Projectile projectile);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
