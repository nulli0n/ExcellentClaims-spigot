package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

public class ChunkClaimEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final World world;
    private final ChunkPos chunkPos;

    private boolean cancelled;

    public ChunkClaimEvent(@NotNull Player player, @NotNull World world, @NotNull ChunkPos chunkPos) {
        this.player = player;
        this.world = world;
        this.chunkPos = chunkPos;
    }

    @NotNull
    public Chunk getChunk() {
        return this.chunkPos.getChunk(this.world);
    }

    @NotNull
    public Player getPlayer() {
        return player;
    }

    @NotNull
    public World getWorld() {
        return world;
    }

    @NotNull
    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
