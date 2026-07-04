package su.nightexpress.excellentclaims.region.member.ui.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.MemberContext;

@NullMarked
public record MemberActionUIContext(RegionClaim claim, MemberContext memberContext) {

}
