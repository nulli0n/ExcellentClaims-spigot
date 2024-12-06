package su.nightexpress.excellentclaims.data.user;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.db.AbstractUser;

import java.util.UUID;

public class ClaimUser extends AbstractUser {

    private boolean adminMode;

    @NotNull
    public static ClaimUser create(@NotNull UUID uuid, @NotNull String name) {
        long lastOnline = System.currentTimeMillis();

        return new ClaimUser(uuid, name, lastOnline, lastOnline);
    }

    public ClaimUser(@NotNull UUID uuid,
                     @NotNull String name,
                     long dateCreated,
                     long lastOnline) {
        super(uuid, name, dateCreated, lastOnline);
    }

    public boolean isAdminMode() {
        return adminMode;
    }

    public void setAdminMode(boolean adminMode) {
        this.adminMode = adminMode;
    }
}
