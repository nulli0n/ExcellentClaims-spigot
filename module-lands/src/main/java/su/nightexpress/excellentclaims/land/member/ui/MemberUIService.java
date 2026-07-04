package su.nightexpress.excellentclaims.land.member.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.member.MemberLang;
import su.nightexpress.excellentclaims.land.member.MemberMenus;
import su.nightexpress.excellentclaims.land.member.MemberService;
import su.nightexpress.excellentclaims.land.member.ui.context.MemberActionUIContext;
import su.nightexpress.excellentclaims.land.member.ui.context.MemberListUIContext;
import su.nightexpress.excellentclaims.land.member.ui.context.OnlineMemberContext;
import su.nightexpress.excellentclaims.land.member.ui.context.RankUpdateContext;
import su.nightexpress.excellentclaims.land.member.ui.dialog.MemberDialogKeys;
import su.nightexpress.excellentclaims.land.ui.LandUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

public class MemberUIService {

    private final DialogRegistry dialogs;
    private final LandUIService  coreUI;
    private final MemberService  service;

    @Nullable
    private MemberMenus menus;

    public MemberUIService(DialogRegistry dialogs, LandUIService landsUIService, MemberService service) {
        this.dialogs = dialogs;
        this.coreUI = landsUIService;
        this.service = service;
    }

    public void registerMenus(MemberMenus menus) {
        this.menus = menus;
    }

    private MemberMenus checkMenus() {
        if (this.menus == null) throw new IllegalStateException("Menus are not initialized!");

        return this.menus;
    }

    public ActionResult showMainMenu(Player player, LandClaim claim) {
        return this.coreUI.showClaimMenu(player, claim);
    }

    public ActionResult showMembersMenu(Player player, LandClaim claim, MemberListUIContext context) {
        ActionResult check = this.service.canManageMembers(player, claim);
        if (!check.success()) return check;

        boolean result = this.checkMenus().membersMenu().show(player, context);
        return ActionResult.of(result);
    }

    public ActionResult showMemberActionsMenu(Player player, LandClaim claim, MemberActionUIContext context) {
        ActionResult check = this.service.canManageMembers(player, claim);
        if (!check.success()) return check;

        boolean result = this.checkMenus().actionsMenu().show(player, context);
        return ActionResult.of(result);
    }

    public ActionResult showAddMemberByOnlinePlayerDialog(Player player, OnlineMemberContext context,
                                                          @Nullable Runnable callback) {
        ActionResult check = this.service.canAddMembers(player, context.claim());
        if (!check.success()) return check;

        if (context.users().isEmpty()) {
            return ActionResult.fail(MemberLang.MEMBER_ADD_ONLINE_NOTHING);
        }

        this.dialogs.show(player, MemberDialogKeys.ADD_ONLINE, context, callback);
        return ActionResult.ok();
    }

    public ActionResult showMemberPurgeDialog(Player player, LandClaim claim, @Nullable Runnable callback) {
        ActionResult check = this.service.canPurgeMembers(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, MemberDialogKeys.PURGE, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult showPromotionDialog(Player player, LandClaim claim, RankUpdateContext context,
                                            @Nullable Runnable callback) {
        ClaimMember member = context.actionContext().memberContext().member();
        ActionResult check = this.service.canPromote(player, claim, member);
        if (!check.success()) return check;

        this.dialogs.show(player, MemberDialogKeys.RANK_UPDATE, context, callback);
        return ActionResult.ok();
    }

    public ActionResult showDemotionDialog(Player player, LandClaim claim, RankUpdateContext context,
                                           @Nullable Runnable callback) {
        ClaimMember member = context.actionContext().memberContext().member();
        ActionResult check = this.service.canDemote(player, claim, member);
        if (!check.success()) return check;

        this.dialogs.show(player, MemberDialogKeys.RANK_UPDATE, context, callback);
        return ActionResult.ok();
    }

    public ActionResult showKickMemberDialog(Player player, LandClaim claim, MemberActionUIContext context,
                                             @Nullable Runnable callback) {
        ClaimMember member = context.memberContext().member();
        ActionResult check = this.service.canKickMember(player, claim, member);
        if (!check.success()) return check;

        this.dialogs.show(player, MemberDialogKeys.KICK, context, callback);
        return ActionResult.ok();
    }
}
