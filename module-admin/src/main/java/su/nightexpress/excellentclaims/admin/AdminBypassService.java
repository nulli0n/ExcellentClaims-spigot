package su.nightexpress.excellentclaims.admin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.admin.AdminBypassAPI;

@NullMarked
public class AdminBypassService implements AdminBypassAPI {

    private final Set<UUID> adminModes;

    public AdminBypassService() {
        this.adminModes = new HashSet<>();
    }

    public void clearSessions() {
        this.adminModes.clear();
    }

    @Override
    public boolean isAdminMode(Player player) {
        return this.isAdminMode(player.getUniqueId());
    }

    @Override
    public boolean isAdminMode(UUID playerId) {
        return this.adminModes.contains(playerId);
    }

    @Override
    public void setAdminMode(Player player, boolean enabled) {
        this.setAdminMode(player.getUniqueId(), enabled);
    }

    @Override
    public void setAdminMode(UUID playerId, boolean enabled) {
        if (enabled) {
            this.adminModes.add(playerId);
        }
        else {
            this.adminModes.remove(playerId);
        }
    }
}
