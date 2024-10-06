package su.nightexpress.excellentclaims.api.event;

import org.bukkit.World;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;

public abstract class ClaimEvent extends Event {

    protected final Claim claim;

    public ClaimEvent(@NotNull Claim claim) {
        this.claim = claim;
    }

    @NotNull
    public Claim getClaim() {
        return this.claim;
    }

    @Nullable
    public World getWorld() {
        return this.claim.getWorld();
    }
}
