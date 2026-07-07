package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerDamageEntityRule extends SimpleSpec<EntityDamageContext, Boolean> {

    protected final ClaimPermissionAPI permissions;

    public BasePlayerDamageEntityRule(ClaimPermissionAPI permissions) {
        super(EntityDamageContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityDamageContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.shouldHandle(context, context.entity()))
            .process((context, registry, resolver) -> {
                Player actor = context.actor();
                if (actor == null) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(context.entity().getLocation());
                if (claim == null) return RuleResult.pass();

                ClaimPermission permission = this.getClaimPermission();
                if (permission != null && this.permissions.hasPermission(actor, claim, permission)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = resolver.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected @Nullable ClaimPermission getClaimPermission() {
        return ClaimPermission.DAMAGE_MOBS;
    }

    protected abstract boolean shouldHandle(EntityDamageContext event, Entity entity);
}
