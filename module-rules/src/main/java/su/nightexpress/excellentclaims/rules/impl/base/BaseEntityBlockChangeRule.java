package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseEntityBlockChangeRule extends SimpleSpec<EntityChangeBlockContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BaseEntityBlockChangeRule(ClaimPermissionAPI permissions) {
        super(EntityChangeBlockContext.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityChangeBlockContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.shouldHandle(context, context.entity(), context.block()))
            .process((context, registry, resolver) -> {
                Player actor = context.actor();
                Claim claim = registry.getPrioritizedClaim(context.block());
                if (actor != null && claim != null) {
                    if (this.permissions.hasPermission(actor, claim, ClaimPermission.BUILDING)) {
                        return RuleResult.allow();
                    }
                }

                Optional<Boolean> state = resolver.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(EntityChangeBlockContext context, Entity entity, Block block);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
