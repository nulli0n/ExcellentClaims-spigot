package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;

public class ChunkMergeEvent extends PlayerChunkEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ChunkClaim target;

    private boolean cancelled;

    public ChunkMergeEvent(@NotNull ChunkClaim source, @NotNull Player player, @NotNull ChunkClaim target) {
        super(source, player);
        this.target = target;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
