package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerPortalRule extends SimpleSpec<PlayerPortalEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BasePlayerPortalRule(ClaimPermissionAPI permissions) {
        super(PlayerPortalEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<PlayerPortalEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> this.shouldHandle(event, event.getCause()))
            .process((event, registry, context) -> {
                Claim claim = registry.getPrioritizedClaim(event.getFrom());
                if (claim == null) return RuleResult.allow();

                Player player = event.getPlayer();
                if (this.permissions.hasPermission(player, claim, ClaimPermission.USE_PORTALS)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_PORTAL_USE));
                }

                return RuleResult.allow();
            })
            .playerExtractor(PlayerPortalEvent::getPlayer)
            .build();
    }

    protected abstract boolean shouldHandle(PlayerPortalEvent event, TeleportCause cause);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
