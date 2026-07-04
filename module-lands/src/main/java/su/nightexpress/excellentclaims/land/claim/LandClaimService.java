package su.nightexpress.excellentclaims.land.claim;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.core.EventPublisher;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.event.land.LandChunkClaimEvent;
import su.nightexpress.excellentclaims.api.event.land.LandChunkClaimedEvent;
import su.nightexpress.excellentclaims.api.event.land.LandChunkUnclaimEvent;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.claim.validation.LandActionValidator;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.util.geodata.pos.ChunkPos;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class LandClaimService {

    private final EventPublisher eventPublisher;

    private final LandSettings    settings;
    private final LandDataService dataService;

    private final LandActionValidator validator;

    @Nullable
    private final LandBillingService billingService;

    public LandClaimService(EventPublisher eventPublisher,
                            LandSettings settings,
                            LandDataService dataService,
                            LandActionValidator validator,
                            @Nullable LandBillingService billingService) {
        this.eventPublisher = eventPublisher;

        this.settings = settings;
        this.dataService = dataService;

        this.validator = validator;
        this.billingService = billingService;
    }

    private String resolveClaimName(Player player, @Nullable String name) {
        if (name != null && !name.isBlank()) {
            return name;
        }

        String defName = this.settings.getDefaultName();

        PlaceholderContext namePlaceholders = PlaceholderContext.builder()
            .with(CommonPlaceholders.PLAYER.resolver(player))
            .build();


        return namePlaceholders.apply(defName);
    }

    public ActionResult claimLand(Player player, Location location, @Nullable String name) {
        World world = location.getWorld();
        if (world == null) {
            return ActionResult.fail(LandLang.CLAIMING_BAD_WORLD);
        }

        String displayName = this.resolveClaimName(player, name);
        String sanitized = Identifier.sanitize(displayName).orElse(null);
        if (sanitized == null) {
            return ActionResult.fail(LandLang.CLAIMING_INVALID_NAME);
        }

        Identifier claimId = Identifier.of(sanitized);
        LandClaim existingClaim = this.dataService.getClaim(claimId);

        ActionResult validation = this.validator.validateClaim(player, world, location, existingClaim);
        if (!validation.success()) return validation;


        if (this.billingService != null) {
            ActionResult billCheck = this.billingService.canAffordClaim(player);
            if (!billCheck.success()) {
                return billCheck;
            }
        }

        ChunkPos chunkPos = ChunkPos.from(location);

        boolean claimEvent = this.eventPublisher.fireEvent(new LandChunkClaimEvent(player, world, chunkPos));
        if (!claimEvent) return ActionResult.fail();

        LandClaim claim;
        if (existingClaim == null) {
            claim = this.dataService.createClaim(claimId, player, world, chunkPos);
            claim.setName(displayName);
            claim.setIcon(this.settings.getDefaultIcon());
            claim.setPriority(this.settings.getDefaultPriority());
        }
        else {
            claim = existingClaim;
            existingClaim.getChunkData().addChunkPosition(chunkPos);

            this.dataService.markDirty(claim);
            this.dataService.updateClaim(existingClaim);
        }

        if (this.billingService != null) {
            this.billingService.chargeForClaim(player); // TODO Inform Player
        }

        this.eventPublisher.fireEvent(new LandChunkClaimedEvent(claim, player));

        return ActionResult.ok(LandLang.CLAIMING_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }

    public ActionResult unclaimChunk(Player player, LandClaim claim) {
        ActionResult validation = this.validator.validateUnclaim(player, claim);
        if (!validation.success()) return validation;

        // TODO Billing Refund?

        boolean unclaimEvent = this.eventPublisher.fireEvent(new LandChunkUnclaimEvent(claim, player));
        if (!unclaimEvent) return ActionResult.fail(); // TODO Msg

        this.dataService.deleteClaim(claim);
        this.eventPublisher.fireEvent(new LandChunkUnclaimEvent(claim, player));

        return ActionResult.ok(LandLang.UNCLAIMING_SUCCESS, ctx -> ctx.with(claim.placeholders()));
    }
}
