package su.nightexpress.excellentclaims.api.member;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.UUID;

public interface Member {

    void write(@NotNull FileConfig config, @NotNull String path);

    boolean isPlayer(@NotNull Player player);

    boolean hasPermission(@NotNull ClaimPermission permission);

    @Nullable Player getPlayer();

    @NotNull OfflinePlayer getOfflinePlayer();

    @NotNull UserInfo getUserInfo();

    @NotNull UUID getPlayerId();

    @NotNull String getPlayerName();

    @NotNull MemberRank getRank();

    void setRank(@NotNull MemberRank rank);
}
