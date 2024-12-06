package su.nightexpress.excellentclaims.api.event.chunk;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.LandClaim;

public class ChunkSeparatedEvent extends PlayerChunkEvent {

    private static final HandlerList handlers = new HandlerList();

    private final LandClaim target;

    public ChunkSeparatedEvent(@NotNull LandClaim source, @NotNull Player player, @NotNull LandClaim target) {
        super(source, player);
        this.target = target;
    }

    @NotNull
    public LandClaim getSource() {
        return this.getChunkClaim();
    }

    @NotNull
    public LandClaim getTarget() {
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
