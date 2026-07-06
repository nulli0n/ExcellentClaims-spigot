package su.nightexpress.excellentclaims.engine.placeholders;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.nightcore.core.config.CoreLang;

public final class EnginePlaceholderConfiguration {

    private EnginePlaceholderConfiguration() {
    }

    public static void configure(DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);
        ClaimRegistry repository = container.get(ClaimRegistry.class);

        plugin.addGlobalPlaceholders(registry -> {
            registry.registerRaw("claim_id", (player, payload) -> {
                Claim claim = repository.getPrioritizedClaim(player);
                return claim == null ? Lang.PLACEHOLDER_NO_CLAIM.text() : claim.getId().value();
            });

            registry.registerRaw("claim_name", (player, payload) -> {
                Claim claim = repository.getPrioritizedClaim(player);
                return claim == null ? Lang.PLACEHOLDER_NO_CLAIM.text() : claim.getName();
            });

            registry.registerRaw("is_claim_member", (player, payload) -> {
                Claim claim = repository.getPrioritizedClaim(player);
                return CoreLang.STATE_YES_NO.get(claim instanceof OwnableClaim ownable && ownable.isMember(player));
            });

            registry.registerRaw("is_claim_owner", (player, payload) -> {
                Claim claim = repository.getPrioritizedClaim(player);
                return CoreLang.STATE_YES_NO.get(claim instanceof OwnableClaim ownable && ownable.isOwner(player));
            });
        });
    }
}
