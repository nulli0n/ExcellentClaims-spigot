package su.nightexpress.excellentclaims.land.claim.validation;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.LandsRepository;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class LandOverlapValidator {

    private final ClaimRegistry      registry;
    private final ClaimPermissionAPI permissions;
    private final LandsRepository    repository;
    private final LandSettings       settings;

    public LandOverlapValidator(ClaimRegistry registry,
                                ClaimPermissionAPI permissions,
                                LandsRepository repository,
                                LandSettings settings) {
        this.registry = registry;
        this.permissions = permissions;
        this.repository = repository;
        this.settings = settings;
    }

    public ActionResult canClaimHere(Player player, Location location) {
        // Check if there is other land claims by chunk position.
        LandClaim landByChunk = this.repository.getPrioritizedClaim(location);
        if (landByChunk != null) {
            if (landByChunk.isOwner(player)) {
                return ActionResult.fail(LandLang.CLAIMING_ALREADY_CLAIMED);
            }

            return ActionResult.fail(LandLang.CLAIMING_ALREADY_OCCUPIED);
        }

        if (!player.hasPermission(LandPerms.BYPASS_CLAIMING_OVERLAP) && !this.permissions.hasBypass(player)) {
            ActionResult overlapResult = this.checkOverlaps(player, location);
            if (!overlapResult.success()) return overlapResult;
        }

        return ActionResult.ok();
    }

    public ActionResult checkOverlaps(Player player, Location location) {
        Set<Claim> overlaps = this.registry.getAt(location);
        if (overlaps.isEmpty()) return ActionResult.ok();

        if (!this.settings.isOverlappingAllowed()) {
            return ActionResult.fail(LandLang.CLAIMING_OVERLAP_FORBIDDEN);
        }

        Claim overlap = overlaps.stream()
            .filter(other -> !this.canOverlapWith(player, other))
            .findFirst()
            .orElse(null);

        if (overlap != null) {
            return ActionResult.fail(LandLang.CLAIMING_OVERLAP_INCOMPATIBLE_CLAIM, ctx -> ctx
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
