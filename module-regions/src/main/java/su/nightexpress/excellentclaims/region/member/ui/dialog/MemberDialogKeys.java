package su.nightexpress.excellentclaims.region.member.ui.dialog;

import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.ui.context.MemberActionUIContext;
import su.nightexpress.excellentclaims.region.member.ui.context.OnlineMemberContext;
import su.nightexpress.excellentclaims.region.member.ui.context.RankUpdateContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public final class MemberDialogKeys {

    public static final DialogKey<RegionClaim>           PURGE       = new DialogKey<>("regions.members.purge");
    public static final DialogKey<MemberActionUIContext> KICK        = new DialogKey<>("regions.members.kick");
    public static final DialogKey<RankUpdateContext>     RANK_UPDATE = new DialogKey<>("regions.members.rank_update");
    public static final DialogKey<OnlineMemberContext>   ADD_ONLINE  = new DialogKey<>("regions.members.add_online");
    public static final DialogKey<RegionClaim>           ADD_BY_NAME = new DialogKey<>("regions.members.add_by_name");

    private MemberDialogKeys() {
    }
}
