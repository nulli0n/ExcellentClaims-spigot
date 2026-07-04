package su.nightexpress.excellentclaims.land.member.ui.context;

import java.util.List;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.ui.list.MemberDisplayData;

public record MemberListUIContext(LandClaim claim, List<MemberDisplayData> members) {

}
