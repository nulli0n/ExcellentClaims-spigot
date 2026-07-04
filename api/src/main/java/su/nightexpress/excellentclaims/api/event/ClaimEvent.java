package su.nightexpress.excellentclaims.api.event;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;

@NullMarked
public abstract class ClaimEvent extends Event {

    protected final Claim claim;

    public ClaimEvent(Claim claim) {
        super();
        this.claim = claim;
    }

    public Claim getClaim() {
        return this.claim;
    }

    @Nullable
    public World getWorld() {
        return this.claim.getWorld();
    }
}
