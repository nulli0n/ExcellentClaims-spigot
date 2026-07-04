package su.nightexpress.excellentclaims.api.claim;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.id.Identifier;

@NullMarked
public interface ClaimMember {

    boolean isPlayer(Player player);

    @Nullable
    Player getPlayer();

    UUID getPlayerId();

    Identifier getRank();

    void setRank(Identifier rank);
}
