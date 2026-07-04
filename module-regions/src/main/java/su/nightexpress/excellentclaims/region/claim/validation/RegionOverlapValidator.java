package su.nightexpress.excellentclaims.region.claim.validation;

import java.util.Set;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RegionOverlapValidator {

    private final ClaimRegistry registry;
    //private final RegionsRepository repository;
    private final RegionSettings settings;

    public RegionOverlapValidator(ClaimRegistry registry, RegionsRepository repository, RegionSettings settings) {
        this.registry = registry;
        //this.repository = repository;
        this.settings = settings;
    }

    public ActionResult canClaimHere(Player player, World world, Cuboid cuboid) {
        if (!player.hasPermission(RegionPerms.BYPASS_CLAIMING_OVERLAP)) {
            ActionResult overlapResult = this.checkOverlaps(player, world, cuboid);
            if (!overlapResult.success()) return overlapResult;
        }

        return ActionResult.ok();
    }

    public ActionResult checkOverlaps(Player player, World world, Cuboid cuboid) {
        Set<Claim> overlaps = this.registry.getInCuboid(world, cuboid);
        if (overlaps.isEmpty()) return ActionResult.ok();

        if (!this.settings.isOverlappingAllowed()) {
            return ActionResult.fail(RegionLang.CLAIMING_OVERLAP_FORBIDDEN);
        }

        Claim overlap = overlaps.stream()
            .filter(other -> !this.canOverlapWith(player, other))
            .findFirst()
            .orElse(null);

        if (overlap != null) {
            return ActionResult.fail(RegionLang.CLAIMING_OVERLAP_INCOMPATIBLE_CLAIM, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_NAME, overlap::getName)
            );
        }

        return ActionResult.ok();
    }

    private boolean canOverlapWith(Player player, Claim with) {
        String withModuleId = with.getModuleKey().value();

        boolean allowed = this.settings.getAllowedOverlappingTypes().contains(withModuleId);
        if (!allowed) return false;

        if (with instanceof OwnableClaim ownable) {
            return ownable.isOwner(player);
        }

        return with.isBackgroundClaim();
    }
}
