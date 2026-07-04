package su.nightexpress.excellentclaims.region.claim.validation;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.DimensionType;

@NullMarked
public class RegionQuotaValidator {

    private final RegionSettings    settings;
    private final RegionsRepository repository;

    public RegionQuotaValidator(RegionSettings settings, RegionsRepository repository) {
        this.settings = settings;
        this.repository = repository;
    }

    public DimensionType getSizeValidationType() {
        return this.settings.isRegion3DSizeValidation() ? DimensionType._3D : DimensionType._2D;
    }

    public ActionResult checkSizeQuota(Player player, Cuboid cuboid) {
        DimensionType type = this.getSizeValidationType();
        int volume = cuboid.getVolume(type);
        int claimBlocks = this.getAvailableClaimBlocks(player);
        if (claimBlocks >= 0 && claimBlocks < volume) {
            return ActionResult.fail(RegionLang.SELECTION_SIZE_QUOTA, ctx -> ctx
                .with(ClaimsPlaceholders.GENERIC_MAX, () -> NumberUtil.format(claimBlocks))
            );
        }

        return ActionResult.ok();
    }

    public boolean canClaimMore(Player player) {
        return this.getAvailableRegions(player) != 0;
    }

    public int getMaxRegions(Player player) {
        return this.settings.getRegionAmountQuota().getGreatestOrNegative(player).intValue();
    }

    public int getAvailableRegions(Player player) {
        int max = this.getMaxRegions(player);
        if (max < 0) return -1;

        int owns = this.repository.countClaims(player);
        return Math.max(0, max - owns);
    }

    public int getMaxClaimBlocks(Player player) {
        return this.settings.getRegionSizeQuota().getGreatestOrNegative(player).intValue();
    }

    public int getAvailableClaimBlocks(Player player) {
        return this.getMaxClaimBlocks(player); // Impl per player storage later
    }
}
