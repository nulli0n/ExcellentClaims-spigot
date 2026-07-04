package su.nightexpress.excellentclaims.region.member;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.nightcore.userdata.UserData;

public record MemberContext(ClaimMember member, UserData userData) {

}
