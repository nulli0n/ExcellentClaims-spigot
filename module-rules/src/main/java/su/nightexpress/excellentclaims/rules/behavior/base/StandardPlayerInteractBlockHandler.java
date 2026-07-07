package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.Material;
import org.bukkit.block.Block;
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
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;

@NullMarked
public abstract class StandardPlayerInteractBlockHandler<T> implements RuleProcessor<BlockInteractContext, T> {

    private final ClaimPermissionAPI permissions;
    private final ClaimPermission    permission;

    public StandardPlayerInteractBlockHandler(ClaimPermissionAPI permissions, ClaimPermission permission) {
        this.permissions = permissions;
        this.permission = permission;
    }

    @Override
    public RuleResult process(BlockInteractContext context, ClaimRegistry registry, RuleLookup<T> resolver) {
        Block block = context.block();

        Claim claim = registry.getPrioritizedClaim(block);
        if (claim == null) return RuleResult.allow();

        Player player = context.actor();
        if (this.permissions.hasPermission(player, claim, this.permission)) {
            return RuleResult.allow();
        }

        T value = resolver.resolveValue(claim).orElse(null);
        if (value == null) return RuleResult.pass();

        if (!this.isBlockAllowed(block.getType(), value)) {
            return RuleResult.deny();
        }

        return RuleResult.allow();
    }

    protected abstract boolean isBlockAllowed(Material type, @NonNull T value);
}
