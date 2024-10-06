package su.nightexpress.excellentclaims.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.nightcore.database.AbstractUser;

import java.util.UUID;

public class ClaimUser extends AbstractUser<ClaimPlugin> {

    private boolean adminMode;
    //private Claim lastClaim;

    @NotNull
    public static ClaimUser create(@NotNull ClaimPlugin plugin, @NotNull UUID uuid, @NotNull String name) {
        long lastOnline = System.currentTimeMillis();

        return new ClaimUser(plugin, uuid, name, lastOnline, lastOnline);
    }

    public ClaimUser(@NotNull ClaimPlugin plugin,
                     @NotNull UUID uuid,
                     @NotNull String name,
                     long dateCreated,
                     long lastOnline) {
        super(plugin, uuid, name, dateCreated, lastOnline);
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public void setAdminMode(boolean adminMode) {
        this.adminMode = adminMode;
    }

//    @Nullable
//    public Claim getLastClaim() {
//        return this.lastClaim;
//    }
//
//    public void setLastClaim(@Nullable Claim lastClaim) {
//        this.lastClaim = lastClaim;
//    }
}
