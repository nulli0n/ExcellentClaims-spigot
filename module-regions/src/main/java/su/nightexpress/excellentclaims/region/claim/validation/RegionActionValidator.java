package su.nightexpress.excellentclaims.region.claim.validation;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RegionActionValidator {

    private final RegionSettings         settings;
    private final ClaimPermissionAPI     permissions;
    private final RegionQuotaValidator   quota;
    private final RegionOverlapValidator overlap;

    public RegionActionValidator(RegionSettings settings,
                                 ClaimPermissionAPI permissions,
                                 RegionQuotaValidator quota,
                                 RegionOverlapValidator overlap) {
        this.settings = settings;
        this.permissions = permissions;
        this.quota = quota;
        this.overlap = overlap;
    }

    public ActionResult validateClaim(Player player,
                                      World world,
                                      Cuboid cuboid,
                                      @Nullable RegionClaim existingByName) {
        if (existingByName != null) {
            return ActionResult.fail(RegionLang.CLAIMING_ALREADY_EXISTS);
        }

        if (this.permissions.hasBypass(player)) {
            return ActionResult.ok();
        }

        ActionResult checkQuota = this.checkQuotas(player);
        if (!checkQuota.success()) return checkQuota;

        // Check World
        if (!player.hasPermission(RegionPerms.BYPASS_CLAIMING_WORLD)) {
            if (this.settings.getClaimBannedWorlds().contains(BukkitThing.getAsString(world))) {
                return ActionResult.fail(RegionLang.CLAIMING_BAD_WORLD);
            }
        }

        // Check Overlap
        return this.overlap.canClaimHere(player, world, cuboid);
    }

    private ActionResult checkQuotas(Player player) {
        if (!this.quota.canClaimMore(player)) {
            return ActionResult.fail(RegionLang.CLAIMING_REGION_QOUTA, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(this.quota.getMaxRegions(player)))
            );
        }

        return ActionResult.ok();
    }

    public ActionResult validateUnclaim(Player player, RegionClaim claim) {
        // Permission Check
        if (!claim.isOwner(player) && !this.permissions.hasBypass(player)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }
}