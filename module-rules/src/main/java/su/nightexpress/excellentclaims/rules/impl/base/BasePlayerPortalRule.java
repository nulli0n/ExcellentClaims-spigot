package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.player.TeleportContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerPortalRule extends SimpleSpec<TeleportContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BasePlayerPortalRule(ClaimPermissionAPI permissions) {
        super(TeleportContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<TeleportContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> this.shouldHandle(context, context.cause()))
            .process((context, registry, resolver) -> {
                Claim claim = registry.getPrioritizedClaim(context.from());
                if (claim == null) return RuleResult.allow();

                Player player = context.actor();
                if (this.permissions.hasPermission(player, claim, ClaimPermission.USE_PORTALS)) {
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

    protected abstract boolean shouldHandle(TeleportContext context, TeleportCause cause);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
