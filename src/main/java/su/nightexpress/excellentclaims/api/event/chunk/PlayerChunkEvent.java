package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.LandClaim;

public abstract class PlayerChunkEvent extends ChunkEvent {

    protected final Player player;

    public PlayerChunkEvent(@NotNull LandClaim landClaim, @NotNull Player player) {
        super(landClaim);
        this.player = player;
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }
}
