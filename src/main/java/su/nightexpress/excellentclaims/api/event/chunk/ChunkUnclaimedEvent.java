package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.LandClaim;

public class ChunkUnclaimedEvent extends PlayerChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChunkUnclaimedEvent(@NotNull LandClaim landClaim, @NotNull Player player) {
        super(landClaim, player);
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
