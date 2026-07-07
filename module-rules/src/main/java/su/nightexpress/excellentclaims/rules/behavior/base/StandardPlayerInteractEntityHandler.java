package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.RuleProcessor;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityInteractContext;

@NullMarked
public abstract class StandardPlayerInteractEntityHandler<T> implements RuleProcessor<EntityInteractContext, T> {

    private final ClaimPermissionAPI permissions;
    private final ClaimPermission    permission;

    public StandardPlayerInteractEntityHandler(ClaimPermissionAPI permissions, ClaimPermission permission) {
        this.permissions = permissions;
        this.permission = permission;
    }

    @Override
    public RuleResult process(EntityInteractContext context, ClaimRegistry registry, RuleLookup<T> resolver) {
        Entity entity = context.entity();

        Claim claim = registry.getPrioritizedClaim(entity.getLocation());
        if (claim == null) return RuleResult.pass();

        Player player = context.actor();
        if (this.permissions.hasPermission(player, claim, this.permission)) {
            return RuleResult.allow();
        }

        T value = resolver.resolveValue(claim).orElse(null);
        if (value == null) return RuleResult.pass();

        if (!this.isEntityAllowed(entity.getType(), value)) {
            return RuleResult.deny();
        }

        return RuleResult.allow();
    }

    protected abstract boolean isEntityAllowed(EntityType type, @NonNull T value);
}
