package su.nightexpress.excellentclaims.member;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimsAPI;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.UUID;

public class ClaimMember implements Member {

    private final UserInfo userInfo;

    private MemberRank rank;

    public ClaimMember(@NotNull UserInfo userInfo, @NotNull MemberRank rank) {
        this.userInfo = userInfo;
        this.setRank(rank);
    }

    public ClaimMember(@NotNull Player player, @NotNull MemberRank rank) {
        this(UserInfo.of(player), rank);
    }

    @Nullable
    public static ClaimMember read(@NotNull FileConfig config, @NotNull String path, @NotNull String id) {
        UserInfo userInfo = UserInfo.read(config, path + ".UserInfo");
        if (userInfo == null) return null;

        String rankId = config.getString(path + ".Rank");

        MemberRank rank = rankId == null ? null : ClaimsAPI.getMemberManager().getRank(rankId);
        if (rank == null) rank = ClaimsAPI.getMemberManager().getLowestRank();

        return new ClaimMember(userInfo, rank);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        this.userInfo.write(config, path + ".UserInfo");
        config.set(path + ".Rank", this.rank.getId());
    }

    @Override
    public boolean isPlayer(@NotNull Player player) {
        return this.getPlayerId().equals(player.getUniqueId());
    }

    @Override
    public boolean hasPermission(@NotNull ClaimPermission permission) {
        return this.rank.hasPermission(permission);
    }

    @Override
    @Nullable
    public Player getPlayer() {
        return this.userInfo.getPlayer();
    }

    @Override
    @NotNull
    public OfflinePlayer getOfflinePlayer() {
        return this.userInfo.getOfflinePlayer();
    }

    @NotNull
    @Override
    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    @NotNull
    @Override
    public UUID getPlayerId() {
        return this.userInfo.getPlayerId();
    }

    @NotNull
    @Override
    public String getPlayerName() {
        return this.userInfo.getPlayerName();
    }

    @NotNull
    @Override
    public MemberRank getRank() {
        return rank;
    }

    @Override
    public void setRank(@NotNull MemberRank rank) {
        this.rank = rank;
    }
}
