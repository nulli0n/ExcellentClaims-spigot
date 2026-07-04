package su.nightexpress.excellentclaims.land.member.ui.context;

import java.util.List;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.userdata.UserData;

public record OnlineMemberContext(LandClaim claim, List<UserData> users) {

}
