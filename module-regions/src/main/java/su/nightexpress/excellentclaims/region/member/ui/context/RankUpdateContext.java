package su.nightexpress.excellentclaims.region.member.ui.context;

import java.util.List;

import su.nightexpress.excellentclaims.api.rank.Rank;

public record RankUpdateContext(MemberActionUIContext actionContext, List<Rank> ranks, boolean promotion) {

}
