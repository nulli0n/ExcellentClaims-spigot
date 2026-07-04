package su.nightexpress.excellentclaims.region.ownership.ui.context;

import java.util.Collection;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.userdata.UserData;

public record TransferTargetContext(RegionClaim claim, Collection<UserData> eligibles) {

}
