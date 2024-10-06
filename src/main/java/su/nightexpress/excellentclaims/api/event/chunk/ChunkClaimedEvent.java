package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;

public class ChunkClaimedEvent extends PlayerChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChunkClaimedEvent(@NotNull ChunkClaim chunkClaim, @NotNull Player player) {
        super(chunkClaim, player);
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
