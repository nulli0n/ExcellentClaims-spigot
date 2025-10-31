package su.nightexpress.excellentclaims.hook.impl;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.config.Lang;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlaceholderHook {

    private static Expansion expansion;

    public static void setup(@NotNull ClaimPlugin plugin) {
        if (expansion == null) {
            expansion = new Expansion(plugin);
            expansion.register();
        }
    }

    public static void shutdown() {
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
        }
    }

    private interface UserPlaceholder {

        @NotNull String supply(@NotNull Player player);
    }

    private static class Expansion extends PlaceholderExpansion {

        private static final String PREFIX = "eclaims";

        private final ClaimPlugin plugin;
        private final Map<String, UserPlaceholder> userPlaceholders;

        public Expansion(@NotNull ClaimPlugin plugin) {
            this.plugin = plugin;
            this.userPlaceholders = new LinkedHashMap<>();

            this.loadUserPlaceholders();
        }

        private void loadUserPlaceholders() {
            this.userPlaceholders.put("lands_claimed", player -> String.valueOf(plugin.getClaimManager().countClaims(player, ClaimType.CHUNK)));
            this.userPlaceholders.put("regions_claimed", player -> String.valueOf(plugin.getClaimManager().countClaims(player, ClaimType.REGION)));

            this.userPlaceholders.put("claim_name", player -> plugin.getClaimManager().getClaimName(player.getLocation(), null));
            this.userPlaceholders.put("land_name", player -> plugin.getClaimManager().getClaimName(player.getLocation(), ClaimType.CHUNK));
            this.userPlaceholders.put("region_name", player -> plugin.getClaimManager().getClaimName(player.getLocation(), ClaimType.REGION));

            this.userPlaceholders.put("claim_id", player -> plugin.getClaimManager().getClaimId(player.getLocation(), null));
            this.userPlaceholders.put("land_id", player -> plugin.getClaimManager().getClaimId(player.getLocation(), ClaimType.CHUNK));
            this.userPlaceholders.put("region_id", player -> plugin.getClaimManager().getClaimId(player.getLocation(), ClaimType.REGION));

            this.userPlaceholders.put("claim_owner", player -> plugin.getClaimManager().getClaimOwnerName(player.getLocation(), null));
            this.userPlaceholders.put("land_owner", player -> plugin.getClaimManager().getClaimOwnerName(player.getLocation(), ClaimType.CHUNK));
            this.userPlaceholders.put("region_owner", player -> plugin.getClaimManager().getClaimOwnerName(player.getLocation(), ClaimType.REGION));

            this.userPlaceholders.put("is_claim_member", player -> {
                Claim claim = this.plugin.getClaimManager().getPrioritizedClaim(player.getLocation());
                return Lang.getYesOrNo(claim != null && claim.isOwnerOrMember(player));
            });

            this.userPlaceholders.put("can_build", player -> Lang.getYesOrNo(plugin.getClaimManager().canBuild(player, player.getLocation())));
        }

        @Override
        @NotNull
        public String getIdentifier() {
            return PREFIX;
        }

        @Override
        @NotNull
        public String getAuthor() {
            return this.plugin.getDescription().getAuthors().getFirst();
        }

        @Override
        @NotNull
        public String getVersion() {
            return this.plugin.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        @Nullable
        public String onPlaceholderRequest(Player player, @NotNull String params) {
            UserPlaceholder placeholder = this.userPlaceholders.get(params);
            if (placeholder != null) {
                return placeholder.supply(player);
            }

            return super.onPlaceholderRequest(player, params);
        }
    }
}
