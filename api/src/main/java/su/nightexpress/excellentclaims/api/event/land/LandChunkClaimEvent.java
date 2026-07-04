package su.nightexpress.excellentclaims.api.event.land;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;

@NullMarked
public class LandChunkClaimEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player   player;
    private final World    world;
    private final ChunkPos chunkPos;

    private boolean cancelled;

    public LandChunkClaimEvent(Player player, World world, ChunkPos chunkPos) {
        super();
        this.player = player;
        this.world = world;
        this.chunkPos = chunkPos;
    }

    public Chunk getChunk() {
        return this.chunkPos.getChunk(this.world);
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

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
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
