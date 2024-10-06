package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ChunkClaim;
import su.nightexpress.excellentclaims.util.pos.ChunkPos;

public class ChunkSeparateEvent extends PlayerChunkEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ChunkPos target;

    private boolean cancelled;

    public ChunkSeparateEvent(@NotNull ChunkClaim source, @NotNull Player player, @NotNull ChunkPos target) {
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
    public ChunkPos getTarget() {
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
