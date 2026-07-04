package su.nightexpress.excellentclaims.land.member.ui.dialog;

import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.ui.context.MemberActionUIContext;
import su.nightexpress.excellentclaims.land.member.ui.context.OnlineMemberContext;
import su.nightexpress.excellentclaims.land.member.ui.context.RankUpdateContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

public final class MemberDialogKeys {

    public static final DialogKey<LandClaim>             PURGE       = new DialogKey<>("lands.members.purge");
    public static final DialogKey<MemberActionUIContext> KICK        = new DialogKey<>("lands.members.kick");
    public static final DialogKey<RankUpdateContext>     RANK_UPDATE = new DialogKey<>("lands.members.rank_update");
    public static final DialogKey<OnlineMemberContext>   ADD_ONLINE  = new DialogKey<>("lands.members.add_online");
    public static final DialogKey<LandClaim>             ADD_BY_NAME = new DialogKey<>("lands.members.add_by_name");

    private MemberDialogKeys() {
    }
}
