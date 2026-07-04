package su.nightexpress.excellentclaims.region.member;

import java.util.List;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.api.service.CommonReason;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMember;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.member.action.MemberReason;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public class MemberService {

    private final RegionDataService  dataService;
    private final ClaimPermissionAPI permissions;
    private final RanksAPI           ranksAPI;

    public MemberService(RegionDataService dataService, ClaimPermissionAPI permissions, RanksAPI ranksAPI) {
        this.dataService = dataService;
        this.permissions = permissions;
        this.ranksAPI = ranksAPI;
    }

    public ActionResult canManageMembers(Player staff, RegionClaim claim) {
        if (!this.permissions.hasPermission(staff, claim, ClaimPermission.MANAGE_MEMBERS)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION, Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canAddMembers(Player staff, RegionClaim claim) {
        if (!this.permissions.hasPermission(staff, claim, ClaimPermission.ADD_MEMBERS)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION, Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canPurgeMembers(Player staff, RegionClaim claim) {
        if (!this.permissions.hasPermission(staff, claim, ClaimPermission.PURGE_MEMBERS)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION, Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        if (claim.getMembers().isEmpty()) {
            return ActionResult.fail(MemberReason.CLEAR_ALREADY_EMPTY, MemberLang.MEMBER_PURGE_ALREADY_EMPTY);
        }

        return ActionResult.ok();
    }

    public ActionResult canKickMember(Player staff, RegionClaim claim, ClaimMember member) {
        if (this.permissions.hasBypass(staff)) return ActionResult.ok();

        if (!this.permissions.hasPermission(staff, claim, ClaimPermission.REMOVE_MEMBERS)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION, Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        ClaimMember staffMember = claim.getMember(staff);

        // Owners do not have ranks and they are not "members"
        if (staffMember != null) {
            Rank staffRank = this.ranksAPI.resolveRank(staffMember);
            Rank memberRank = this.ranksAPI.resolveRank(member);
            if (staffRank.isEqual(memberRank) || staffRank.isBehind(memberRank)) {
                return ActionResult.fail(MemberLang.MEMBER_KICK_TARGET_IS_ABOVE);
            }
        }
        else {
            if (!claim.isOwner(staff)) {
                return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
            }
        }

        return ActionResult.ok();
    }

    public ActionResult canPromote(Player staff, RegionClaim claim, ClaimMember member) {
        if (!this.permissions.hasPermission(staff, claim, ClaimPermission.PROMOTE_MEMBERS)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION, Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        List<Rank> promotionRanks = this.getPromotionRanks(staff, claim, member);
        if (promotionRanks.isEmpty()) {
            return ActionResult.fail(MemberReason.PROMOTE_NOTHING_TO, MemberLang.MEMBER_PROMOTION_NOTHING_TO);
        }

        return ActionResult.ok();
    }

    public ActionResult canDemote(Player staff, RegionClaim claim, ClaimMember member) {
        if (!this.permissions.hasPermission(staff, claim, ClaimPermission.DEMOTE_MEMBERS)) {
            return ActionResult.fail(CommonReason.NO_PERMISSION, Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        List<Rank> demotionRanks = this.getDemotionRanks(staff, claim, member);
        if (demotionRanks.isEmpty()) {
            return ActionResult.fail(MemberReason.DEMOTE_NOTHING_TO, MemberLang.MEMBER_DEMOTION_NOTHING_TO);
        }

        return ActionResult.ok();
    }

    public List<Rank> getPromotionRanks(Player staff, RegionClaim claim, ClaimMember member) {
        boolean adminMode = this.permissions.hasBypass(staff);

        if (claim.isOwner(member.getPlayerId())) return List.of();

        Rank memberRank = this.ranksAPI.resolveRank(member);
        List<Rank> nextRanks = this.ranksAPI.getNextRanks(memberRank);
        if (adminMode || nextRanks.isEmpty()) return nextRanks;

        ClaimMember staffMember = claim.getMember(staff);

        // Owners do not have ranks and they are not "members"
        if (staffMember != null) {
            Rank staffRank = this.ranksAPI.resolveRank(staffMember);
            if (memberRank.isEqual(staffRank) || memberRank.isAbove(staffRank)) return List.of();

            return nextRanks.stream().filter(next -> staffRank.isEqual(next) || staffRank.isBehind(next)).toList();
        }
        else {
            if (!claim.isOwner(staff)) {
                return List.of();
            }
        }

        return nextRanks;
    }

    public List<Rank> getDemotionRanks(Player staff, RegionClaim claim, ClaimMember member) {
        boolean adminMode = this.permissions.hasBypass(staff);

        if (claim.isOwner(member.getPlayerId())) return List.of();

        Rank memberRank = this.ranksAPI.resolveRank(member);
        List<Rank> prevRanks = this.ranksAPI.getPreviousRanks(memberRank);
        if (adminMode || prevRanks.isEmpty()) return prevRanks;

        ClaimMember staffMember = claim.getMember(staff);

        // Owners do not have ranks and they are not "members"
        if (staffMember != null) {
            Rank staffRank = this.ranksAPI.resolveRank(staffMember);
            if (memberRank.isEqual(staffRank) || memberRank.isAbove(staffRank)) return List.of();

            return prevRanks;
        }
        else {
            if (!claim.isOwner(staff)) {
                return List.of();
            }
        }

        return prevRanks;
    }

    public ActionResult addMember(Player player, RegionClaim claim, UserData user) {
        if (claim.isOwner(user.getId())) {
            return ActionResult.fail(MemberLang.MEMBER_ADD_TARGET_IS_OWNER, ctx -> ctx
                .with(claim.placeholders())
                .with(CommonPlaceholders.PLAYER_NAME, () -> user.getName())
            );
        }

        if (claim.isMember(user.getId())) {
            return ActionResult.fail(MemberLang.MEMBER_ADD_TARGET_IS_MEMBER, ctx -> ctx
                .with(claim.placeholders())
                .with(CommonPlaceholders.PLAYER_NAME, () -> user.getName())
            );
        }

        Rank rank = this.ranksAPI.getRanks().getLowestRank();
        ClaimMember member = new DefaultClaimMember(user.getId(), rank.id());
        claim.addMember(member);

        this.dataService.markDirty(claim);

        return ActionResult.ok(MemberLang.MEMBER_ADD_SUCCESS, ctx -> ctx
            .with(rank.placeholders())
            .with(claim.placeholders())
            .with(CommonPlaceholders.PLAYER_NAME, () -> user.getName()));
    }

    public ActionResult purgeMembers(Player player, RegionClaim claim) {
        ActionResult check = this.canPurgeMembers(player, claim);
        if (!check.success()) return check;

        claim.removeMembers();
        this.dataService.markDirty(claim);

        return ActionResult.ok(MemberLang.MEMBER_PURGE_SUCCESS, ctx -> ctx
            .with(claim.placeholders())
        );
    }

    public ActionResult kickMember(Player player, RegionClaim claim, MemberContext context) {
        ClaimMember member = context.member();

        ActionResult check = this.canKickMember(player, claim, member);
        if (!check.success()) return check;

        if (!claim.isMember(member.getPlayerId())) {
            return ActionResult.fail(MemberLang.MEMBER_KICK_TARGET_IS_NOT_MEMBER, ctx -> ctx
                .with(claim.placeholders())
                .with(CommonPlaceholders.PLAYER_NAME, () -> context.userData().getName())
            );
        }

        claim.removeMember(member);
        this.dataService.markDirty(claim);

        return ActionResult.ok(MemberLang.MEMBER_KICK_SUCCESS, ctx -> ctx
            .with(claim.placeholders())
            .with(CommonPlaceholders.PLAYER_NAME, () -> context.userData().getName()));
    }

    public ActionResult promoteMember(Player staff, RegionClaim claim, MemberContext context, Rank next) {
        ClaimMember member = context.member();

        ActionResult checkAccess = this.canPromote(staff, claim, member);
        if (!checkAccess.success()) return checkAccess;

        member.setRank(next.id());
        this.dataService.markDirty(claim);

        return ActionResult.ok(MemberLang.MEMBER_PROMOTION_SUCCESS, ctx -> ctx
            .with(CommonPlaceholders.PLAYER_NAME, () -> context.userData().getName())
            .with(next.placeholders())
        );
    }

    public ActionResult demoteMember(Player staff, RegionClaim claim, MemberContext context, Rank prev) {
        ClaimMember member = context.member();

        ActionResult checkAccess = this.canDemote(staff, claim, member);
        if (!checkAccess.success()) return checkAccess;

        member.setRank(prev.id());
        this.dataService.markDirty(claim);

        return ActionResult.ok(MemberLang.MEMBER_DEMOTION_SUCCESS, ctx -> ctx
            .with(CommonPlaceholders.PLAYER_NAME, () -> context.userData().getName())
            .with(prev.placeholders())
        );
    }
}
