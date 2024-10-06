package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;

public class ChunkSeparatedEvent extends PlayerChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    private final ChunkClaim target;

    public ChunkSeparatedEvent(@NotNull ChunkClaim source, @NotNull Player player, @NotNull ChunkClaim target) {
        super(source, player);
        this.target = target;
    }

    @NotNull
    public ChunkClaim getSource() {
        return this.getChunkClaim();
    }

    @NotNull
    public ChunkClaim getTarget() {
        return this.target;
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
