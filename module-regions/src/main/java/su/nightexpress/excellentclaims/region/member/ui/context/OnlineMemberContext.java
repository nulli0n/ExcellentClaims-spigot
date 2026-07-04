package su.nightexpress.excellentclaims.region.member.ui.context;

import java.util.List;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.userdata.UserData;

public record OnlineMemberContext(RegionClaim claim, List<UserData> users) {

}
