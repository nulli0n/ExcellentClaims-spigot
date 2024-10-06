package su.nightexpress.excellentclaims.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.nightcore.database.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<ClaimPlugin, ClaimUser> {

    public UserManager(@NotNull ClaimPlugin plugin) {
        super(plugin);
    }

    @Override
    @NotNull
    public ClaimUser createUserData(@NotNull UUID uuid, @NotNull String name) {
        return ClaimUser.create(plugin, uuid, name);
    }
}
