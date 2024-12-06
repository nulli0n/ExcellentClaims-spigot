package su.nightexpress.excellentclaims.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.data.user.ClaimUser;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.UUID;

public class UserInfo {

    private final UUID playerId;

    private String playerName;

    public UserInfo(@NotNull UUID playerId, @NotNull String playerName) {
        this.playerId = playerId;
        this.playerName = playerName;
    }

    @NotNull
    public static UserInfo of(@NotNull Player player) {
        return new UserInfo(player.getUniqueId(), player.getName());
    }

    @NotNull
    public static UserInfo of(@NotNull ClaimUser user) {
        return new UserInfo(user.getId(), user.getName());
    }

    @Nullable
    public static UserInfo read(@NotNull FileConfig config, @NotNull String path) {
        try {
            UUID playerId = UUID.fromString(config.getString(path + ".PlayerId", "null"));
            String playerName = config.getString(path + ".PlayerName", "null");

            return new UserInfo(playerId, playerName);
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".PlayerId", this.playerId.toString());
        config.set(path + ".PlayerName", this.playerName);
    }

    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerId);
    }

    @NotNull
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.playerId);
    }

    @NotNull
    public UUID getPlayerId() {
        return playerId;
    }

    @NotNull
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(@NotNull String playerName) {
        this.playerName = playerName;
    }

    public boolean updatePlayerName(@NotNull Player player) {
        if (this.playerId.equals(player.getUniqueId()) && !this.playerName.equalsIgnoreCase(player.getName())) {
            this.setPlayerName(player.getName());
            return true;
        }
        return false;
    }
}
