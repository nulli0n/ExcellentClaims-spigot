package su.nightexpress.excellentclaims.land.claim.validation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class LandActionValidator {

    private final LandSettings         settings;
    private final ClaimPermissionAPI   permissions;
    private final LandQuotaValidator   quota;
    private final LandOverlapValidator overlap;

    public LandActionValidator(LandSettings settings,
                               ClaimPermissionAPI permissions,
                               LandQuotaValidator quota,
                               LandOverlapValidator overlap) {
        this.settings = settings;
        this.permissions = permissions;
        this.quota = quota;
        this.overlap = overlap;
    }

    public ActionResult validateClaim(Player player,
                                      World world,
                                      Location location,
                                      @Nullable LandClaim existingByName) {
        if (existingByName != null) {
            if (!existingByName.isOwner(player)) {
                return ActionResult.fail(Lang.ERROR_NOT_CLAIM_OWNER);
            }

            if (existingByName.contains(ChunkPos.from(location))) {
                return ActionResult.fail(LandLang.CLAIMING_ALREADY_CLAIMED, ctx -> ctx.with(existingByName
                    .placeholders()));
            }
        }

        ActionResult checkQuota = this.checkQuotas(player, existingByName);
        if (!checkQuota.success()) return checkQuota;


        // Check World
        if (!player.hasPermission(LandPerms.BYPASS_CLAIMING_WORLD)) {
            if (this.settings.getClaimBannedWorlds().contains(BukkitThing.getAsString(world))) {
                return ActionResult.fail(LandLang.CLAIMING_BAD_WORLD);
            }
        }

        // Check Overlap
        return this.overlap.canClaimHere(player, location);
    }

    private ActionResult checkQuotas(Player player, @Nullable LandClaim existingByName) {
        if (existingByName == null && !this.quota.canClaimMore(player)) {
            return ActionResult.fail(LandLang.CLAIMING_LAND_QOUTA, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(this.quota.getMaxClaims(player)))
            );
        }

        if (existingByName != null && !this.quota.canExpandClaim(player, existingByName)) {
            return ActionResult.fail(LandLang.CLAIMING_CHUNK_QUOTA, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(this.quota.getMaxClaimSize(player)))
            );
        }

        return ActionResult.ok();
    }

    public ActionResult validateUnclaim(Player player, LandClaim claim) {
        // Permission Check
        if (!claim.isOwner(player) && !this.permissions.hasBypass(player)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }
}