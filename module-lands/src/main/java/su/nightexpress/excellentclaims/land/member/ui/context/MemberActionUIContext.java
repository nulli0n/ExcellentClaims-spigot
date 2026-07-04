package su.nightexpress.excellentclaims.land.member.ui.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.MemberContext;

@NullMarked
public record MemberActionUIContext(LandClaim claim, MemberContext memberContext) {

}
