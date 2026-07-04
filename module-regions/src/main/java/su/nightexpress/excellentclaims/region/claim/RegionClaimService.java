package su.nightexpress.excellentclaims.region.claim;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.event.region.RegionCreateEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionCreatedEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemoveEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemovedEvent;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.claim.validation.RegionActionValidator;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.geodata.Cuboid;

@NullMarked
public class RegionClaimService {

    private final EventPublisher eventPublisher;

    private final RegionSettings    settings;
    private final RegionDataService dataService;

    private final RegionActionValidator validator;

    @Nullable
    private final RegionBillingService billingService;

    public RegionClaimService(EventPublisher eventPublisher,
                              RegionSettings settings,
                              RegionDataService dataService,
                              RegionActionValidator validator,
                              @Nullable RegionBillingService billingService) {
        this.eventPublisher = eventPublisher;

        this.settings = settings;
        this.dataService = dataService;

        this.validator = validator;
        this.billingService = billingService;
    }

    public ActionResult claimRegion(Player player, World world, Cuboid cuboid, String name) {
        String sanitized = Identifier.sanitize(name).orElse(null);
        if (sanitized == null) {
            return ActionResult.fail(RegionLang.CLAIMING_INVALID_NAME);
        }

        Identifier regionId = Identifier.of(sanitized);
        RegionClaim existingRegion = this.dataService.getClaim(regionId);

        ActionResult validation = this.validator.validateClaim(player, world, cuboid, existingRegion);
        if (!validation.success()) return validation;

        if (this.billingService != null) {
            ActionResult billCheck = this.billingService.canAffordClaim(player);
            if (!billCheck.success()) {
                return billCheck;
            }
        }

        boolean claimEvent = this.eventPublisher.fireEvent(new RegionCreateEvent(player, world, cuboid, regionId));
        if (!claimEvent) return ActionResult.fail();

        RegionClaim region = this.dataService.createRegion(regionId, player, world, cuboid);
        region.setName(StringUtil.capitalizeUnderscored(regionId.value()));
        region.setIcon(this.settings.getDefaultIcon());
        region.setPriority(this.settings.getDefaultPriority());

        if (this.billingService != null) {
            this.billingService.chargeForClaim(player); // TODO Inform Player
        }

        this.eventPublisher.fireEvent(new RegionCreatedEvent(region, player));

        return ActionResult.ok(RegionLang.CLAIMING_SUCCESS, ctx -> ctx.with(region.placeholders()));
    }

    public ActionResult unclaimChunk(Player player, RegionClaim region) {
        ActionResult validation = this.validator.validateUnclaim(player, region);
        if (!validation.success()) return validation;

        // TODO Billing Refund?

        boolean unclaimEvent = this.eventPublisher.fireEvent(new RegionRemoveEvent(region, player));
        if (!unclaimEvent) return ActionResult.fail(); // TODO Msg

        this.dataService.deleteClaim(region);
        this.eventPublisher.fireEvent(new RegionRemovedEvent(region, player));

        return ActionResult.ok(RegionLang.UNCLAIMING_SUCCESS, ctx -> ctx.with(region.placeholders()));
    }
}
