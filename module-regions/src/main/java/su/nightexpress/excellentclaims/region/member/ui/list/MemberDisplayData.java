package su.nightexpress.excellentclaims.region.member.ui.list;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.nightcore.userdata.UserData;

@NullMarked
public record MemberDisplayData(ClaimMember member, UserData userData, Rank rank) {

}