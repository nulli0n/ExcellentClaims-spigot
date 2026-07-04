package su.nightexpress.excellentclaims.api.rank;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;

@NullMarked
public interface RanksAPI {

    RankRegistry getRanks();

    Rank resolveRank(ClaimMember member);

    @Nullable
    Rank resolveRank(OwnableClaim claim, UUID memberId);

    @Nullable
    Rank getNextRank(Rank rank);

    List<Rank> getNextRanks(Rank rank);

    @Nullable
    Rank getPreviousRank(Rank rank);

    List<Rank> getPreviousRanks(Rank rank);
}
