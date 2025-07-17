package su.nightexpress.excellentclaims.flag;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimsAPI;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;

import java.util.function.Predicate;

public class FlagPredicate implements Predicate<Claim> {

    private final ClaimFlag<Boolean> flag;
    private final ClaimPermission permission;
    private final Player player;

    public FlagPredicate(@NotNull ClaimFlag<Boolean> flag, @Nullable ClaimPermission permission, @Nullable Player player) {
        this.flag = flag;
        this.permission = permission;
        this.player = player;
    }

    @Override
    public boolean test(@NotNull Claim claim) {
        if (this.player != null) {
            if (ClaimsAPI.isAdminMode(this.player)) return true;

            if (claim.isOwnerOrMember(this.player)) {
                if (this.permission == null || claim.hasPermission(this.player, this.permission)) {
                    return true;
                }
                //return this.permission == null || claim.hasPermission(this.player, this.permission);
            }
        }

        return !claim.hasFlag(this.flag) || claim.getFlag(this.flag);
    }

    @NotNull
    public ClaimFlag<Boolean> getFlag() {
        return this.flag;
    }

    @Nullable
    public ClaimPermission getPermission() {
        return this.permission;
    }

    @Nullable
    public Player getPlayer() {
        return this.player;
    }
}
