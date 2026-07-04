package su.nightexpress.excellentclaims.land.ui.context;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.userdata.UserData;

@NullMarked
public record InspectContext(UserData user, List<LandClaim> claims) {

}
