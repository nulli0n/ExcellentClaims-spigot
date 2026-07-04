package su.nightexpress.excellentclaims.land.ownership.ui.context;

import java.util.Collection;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.userdata.UserData;

public record TransferTargetContext(LandClaim claim, Collection<UserData> eligibles) {

}
