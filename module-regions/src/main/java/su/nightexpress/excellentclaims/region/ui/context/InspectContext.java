package su.nightexpress.excellentclaims.region.ui.context;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.userdata.UserData;

@NullMarked
public record InspectContext(UserData user, List<RegionClaim> regions) {

}
