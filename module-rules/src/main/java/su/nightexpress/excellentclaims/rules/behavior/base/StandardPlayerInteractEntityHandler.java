package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleContext;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.RuleProcessor;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public abstract class StandardPlayerInteractEntityHandler<T> implements RuleProcessor<PlayerInteractAtEntityEvent, T> {

    private final ClaimPermissionAPI permissions;
    private final ClaimPermission    permission;

    public StandardPlayerInteractEntityHandler(ClaimPermissionAPI permissions, ClaimPermission permission) {
        this.permissions = permissions;
        this.permission = permission;
    }

    @Override
    public RuleResult process(PlayerInteractAtEntityEvent event, ClaimRegistry registry, RuleContext<T> context) {
        Claim claim = registry.getPrioritizedClaim(event.getRightClicked().getLocation());
        if (claim == null) return RuleResult.pass();

        Player player = event.getPlayer();
        if (this.permissions.hasPermission(player, claim, this.permission)) {
            return RuleResult.allow();
        }

        EntityType type = event.getRightClicked().getType();
        T value = context.resolveValue(claim).orElse(null);
        if (value == null) return RuleResult.pass();

        if (!this.isEntityAllowed(type, value)) {
            return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_ENTITY_INTERACT, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(type)))
            );
        }

        return RuleResult.allow();
    }

    protected abstract boolean isEntityAllowed(EntityType type, @NonNull T value);
}
