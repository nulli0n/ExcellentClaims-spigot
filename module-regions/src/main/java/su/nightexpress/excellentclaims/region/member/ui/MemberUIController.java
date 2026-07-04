package su.nightexpress.excellentclaims.region.member.ui;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.MemberContext;
import su.nightexpress.excellentclaims.region.member.MemberService;
import su.nightexpress.excellentclaims.region.member.ui.context.MemberActionUIContext;
import su.nightexpress.excellentclaims.region.member.ui.context.OnlineMemberContext;
import su.nightexpress.excellentclaims.region.member.ui.context.RankUpdateContext;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.userdata.UserData;

public class MemberUIController {

    private final MemberService          memberService;
    private final MemberUIService        uiService;
    private final MemberUIContextFactory contextFactory;
    private final MessageDispatcher      dispatcher;

    public MemberUIController(MemberService memberService,
                              MemberUIService uiService,
                              MemberUIContextFactory contextFactory,
                              MessageDispatcher dispatcher) {
        this.memberService = memberService;
        this.uiService = uiService;
        this.contextFactory = contextFactory;
        this.dispatcher = dispatcher;
    }

    public void openMembersMenu(Player player, RegionClaim claim) {
        ActionResult accessCheck = this.memberService.canManageMembers(player, claim);
        if (!accessCheck.success()) {
            accessCheck.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
            return;
        }

        this.contextFactory.createMembersContext(claim, context -> {
            this.uiService.showMembersMenu(player, claim, context);
        });
    }

    public void onBackClick(Player player, RegionClaim claim) {
        this.uiService.showMainMenu(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onMemberClick(Player player, RegionClaim claim, ClaimMember member, UserData userData) {
        MemberActionUIContext context = new MemberActionUIContext(claim, new MemberContext(member, userData));
        this.uiService.showMemberActionsMenu(player, claim, context).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onAddOnlineMemberClick(Player player, RegionClaim claim, @Nullable Runnable callback) {
        OnlineMemberContext context = this.contextFactory.createOnlineMembersContext(claim);

        this.uiService.showAddMemberByOnlinePlayerDialog(player, context, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onSelectMemberToAdd(Player player, RegionClaim claim, UUID playerId) {
        this.contextFactory.resolveUserContext(playerId, opt -> {
            if (opt.isEmpty()) {
                this.dispatcher.send(CoreLang.ERROR_INVALID_PLAYER, player);
                return;
            }

            this.memberService.addMember(player, claim, opt.get()).handleFeedback((locale, ctx) -> {
                this.dispatcher.send(locale, player, ctx);
            });
        });
    }

    public void onPurgeMembersClick(Player player, RegionClaim claim, @Nullable Runnable callback) {
        this.uiService.showMemberPurgeDialog(player, claim, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onPurgeMembersConfirm(Player player, RegionClaim claim) {
        this.memberService.purgeMembers(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onPromoteMemberClick(Player player, RegionClaim claim, MemberActionUIContext context,
                                     @Nullable Runnable callback) {
        ClaimMember member = context.memberContext().member();
        List<Rank> ranks = this.memberService.getPromotionRanks(player, claim, member);
        RankUpdateContext rankContext = new RankUpdateContext(context, ranks, true);

        this.uiService.showPromotionDialog(player, claim, rankContext, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onPromotionRankSelect(Player player, RegionClaim claim, MemberContext context, Rank rank) {
        this.memberService.promoteMember(player, claim, context, rank).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onDemoteMemberClick(Player player, RegionClaim claim, MemberActionUIContext context,
                                    @Nullable Runnable callback) {
        ClaimMember member = context.memberContext().member();
        List<Rank> ranks = this.memberService.getDemotionRanks(player, claim, member);
        RankUpdateContext rankContext = new RankUpdateContext(context, ranks, false);

        this.uiService.showDemotionDialog(player, claim, rankContext, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onDemotionRankSelect(Player player, RegionClaim claim, MemberContext context, Rank rank) {
        this.memberService.demoteMember(player, claim, context, rank).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onKickMemberClick(Player player, RegionClaim claim, MemberActionUIContext context,
                                  @Nullable Runnable callback) {
        this.uiService.showKickMemberDialog(player, claim, context, callback).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onKickMemberConfirm(Player player, RegionClaim claim, MemberContext context) {
        this.memberService.kickMember(player, claim, context).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
