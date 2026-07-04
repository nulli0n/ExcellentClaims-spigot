package su.nightexpress.excellentclaims.region.member.ui.context;

import java.util.List;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.ui.list.MemberDisplayData;

public record MemberListUIContext(RegionClaim claim, List<MemberDisplayData> members) {

}
