package su.nightexpress.excellentclaims.data.user;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.data.storage.DataManager;
import su.nightexpress.nightcore.db.AbstractUserManager;

import java.util.UUID;

public class UserManager extends AbstractUserManager<ClaimPlugin, ClaimUser> {

    public UserManager(@NotNull ClaimPlugin plugin, @NotNull DataManager dataManager) {
        super(plugin, dataManager);
    }

    @Override
    @NotNull
    public ClaimUser create(@NotNull UUID uuid, @NotNull String name) {
        return ClaimUser.create(uuid, name);
    }
}
