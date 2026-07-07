package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.rules.behavior.base.StandardPlayerInteractEntityHandler;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityInteractContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerUseEntityRule extends SimpleSpec<EntityInteractContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BasePlayerUseEntityRule(ClaimPermissionAPI permissions) {
        super(EntityInteractContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityInteractContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(event -> this.shouldHandle(event, event.entity()))
            .process(
                new StandardPlayerInteractEntityHandler<Boolean>(this.permissions, this.getClaimPermission()) {

                    @Override
                    protected boolean isEntityAllowed(EntityType type, Boolean allowed) {
                        return allowed;
                    }

                })
            .build();
    }

    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.ENTITY_INTERACT;
    }

    protected abstract boolean shouldHandle(EntityInteractContext context, Entity entity);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
