package su.nightexpress.excellentclaims.core.claim;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class DefaultClaimMember implements ClaimMember {

    private final UUID userInfo;

    private Identifier rank;

    public DefaultClaimMember(UUID userInfo, Identifier rank) {
        this.userInfo = userInfo;
        this.rank = rank;
    }

    public DefaultClaimMember(Player player, Identifier rank) {
        this(player.getUniqueId(), rank);
    }

    @Override
    public boolean isPlayer(Player player) {
        return this.getPlayerId().equals(player.getUniqueId());
    }

    /* @Override
    public boolean hasPermission(ClaimPermission permission) {
        return this.rank.hasPermission(permission);
    } */

    @Override
    public @Nullable Player getPlayer() {
        return Players.getPlayer(this.userInfo);
    }

    @Override
    public UUID getPlayerId() {
        return this.userInfo;
    }

    @Override
    public Identifier getRank() {
        return rank;
    }

    @Override
    public void setRank(Identifier rank) {
        this.rank = rank;
    }
}
