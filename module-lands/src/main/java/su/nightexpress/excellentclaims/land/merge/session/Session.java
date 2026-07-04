package su.nightexpress.excellentclaims.land.merge.session;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.land.MergeType;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class Session {

    private final UUID      playerId;
    private final LandClaim claim;
    private final MergeType type;
    private final long      validUntil;

    public Session(UUID playerId, LandClaim claim, MergeType type, long validUntil) {
        this.playerId = playerId;
        this.claim = claim;
        this.type = type;
        this.validUntil = validUntil;
    }

    public boolean isValid() {
        return !TimeUtil.isPassed(this.validUntil);
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public LandClaim getClaim() {
        return claim;
    }

    public MergeType getType() {
        return type;
    }

    public long getValidUntil() {
        return validUntil;
    }
}
