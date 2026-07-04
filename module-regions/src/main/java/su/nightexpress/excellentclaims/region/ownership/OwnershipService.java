package su.nightexpress.excellentclaims.region.ownership;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.core.claim.DefaultClaimMember;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class OwnershipService {

    private final RegionDataService    dataService;
    private final RegionQuotaValidator quotaValidator;
    private final ClaimPermissionAPI   permissions;
    private final RanksAPI             ranksAPI;

    public OwnershipService(RegionDataService dataService,
                            RegionQuotaValidator quotaValidator,
                            ClaimPermissionAPI permissions,
                            RanksAPI ranks) {
        this.dataService = dataService;
        this.quotaValidator = quotaValidator;
        this.permissions = permissions;
        this.ranksAPI = ranks;
    }

    public ActionResult canTransferOwnership(Player player, RegionClaim claim) {
        if (!claim.isOwner(player) && !this.permissions.hasBypass(player)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canTransferOwnership(Player player, RegionClaim claim, Player target) {
        ActionResult check = this.canTransferOwnership(player, claim);
        if (!check.success()) return check;

        if (player == target) {
            return ActionResult.fail(OwnershipLang.TRANSFER_YOURSELF, ctx -> ctx.with(claim.placeholders()));
        }

        if (claim.isOwner(target)) {
            return ActionResult.fail(OwnershipLang.TRANSFER_TARGET_OWNER, ctx -> ctx
                .with(claim.placeholders())
                .with(CommonPlaceholders.PLAYER_NAME, target::getName)
            );
        }

        if (!this.quotaValidator.canClaimMore(player)) {
            return ActionResult.fail(OwnershipLang.TRANSFER_TARGET_QUOTA_LIMIT, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> String.valueOf(this.quotaValidator.getMaxRegions(
                    player)))
                .with(CommonPlaceholders.PLAYER.resolver(target))
            );
        }

        return ActionResult.ok();
    }

    public ActionResult transferOwnership(Player player, RegionClaim claim, Player target) {
        ActionResult check = this.canTransferOwnership(player, claim, target);
        if (!check.success()) return check;

        Rank rank = this.ranksAPI.getRanks().getLowestRank(); // TODO getDefaultRole()

        claim.removeMember(target);
        claim.addMember(new DefaultClaimMember(player, rank.id()));
        claim.removeOwner(player);
        claim.addOwner(target);

        this.dataService.markDirty(claim);
        this.dataService.updateClaim(claim);

        return ActionResult.ok(OwnershipLang.TRANSFER_SUCCESS, ctx -> ctx
            .with(claim.placeholders())
            .with(CommonPlaceholders.PLAYER.resolver(target))
        );
    }
}
