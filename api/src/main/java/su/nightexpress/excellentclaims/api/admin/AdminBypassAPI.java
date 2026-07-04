package su.nightexpress.excellentclaims.api.admin;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface AdminBypassAPI {

    boolean isAdminMode(Player player);

    boolean isAdminMode(UUID playerId);

    void setAdminMode(Player player, boolean enabled);

    void setAdminMode(UUID playerId, boolean enabled);
}
