package su.nightexpress.excellentclaims.permission;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.admin.AdminBypassAPI;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;

@NullMarked
public class PermissionService implements ClaimPermissionAPI {

    @Nullable
    private final RanksAPI ranksAPI;

    @Nullable
    private final AdminBypassAPI bypassAPI;

    public PermissionService(@Nullable RanksAPI ranksAPI, @Nullable AdminBypassAPI bypassAPI) {
        this.ranksAPI = ranksAPI;
        this.bypassAPI = bypassAPI;
    }

    @Override
    public boolean hasBypass(Player player) {
        return this.bypassAPI != null && this.bypassAPI.isAdminMode(player);
    }

    @Override
    public boolean hasPermission(Player player, Claim claim, ClaimPermission permission) {
        if (this.bypassAPI != null && this.bypassAPI.isAdminMode(player)) return true;

        if (claim instanceof OwnableClaim ownable) {
            if (ownable.isOwner(player)) return true;

            Rank rank = this.ranksAPI == null ? null : this.ranksAPI.resolveRank(ownable, player.getUniqueId());
            return rank != null && rank.hasPermission(permission);
        }

        return false;
    }
}
