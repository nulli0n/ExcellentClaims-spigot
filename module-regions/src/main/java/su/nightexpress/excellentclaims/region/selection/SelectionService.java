package su.nightexpress.excellentclaims.region.selection;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.claim.RegionClaimService;
import su.nightexpress.excellentclaims.region.claim.validation.RegionQuotaValidator;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.selection.session.SelectionSession;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.geodata.Cuboid;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class SelectionService {

    private final RegionQuotaValidator quotaValidator;
    private final RegionClaimService   claimService;

    public SelectionService(RegionQuotaValidator quotaValidator,
                            RegionClaimService claimService) {
        this.quotaValidator = quotaValidator;
        this.claimService = claimService;
    }

    public ActionResult selectBlockPosition(Player player,
                                            SelectionSession session,
                                            BlockPos blockPos,
                                            SelectionPosition position) {

        BlockPos first;
        BlockPos second;
        MessageLocale locale;

        if (position == SelectionPosition.FIRST) {
            first = blockPos;
            second = session.getSecond();
            locale = RegionLang.SELECTION_SET_FIRST;
        }
        else {
            first = session.getFirst();
            second = blockPos;
            locale = RegionLang.SELECTION_SET_SECOND;
        }

        // Check selection size
        if (first != null && second != null) {
            Cuboid cuboid = new Cuboid(first, second);

            ActionResult sizeCheck = this.quotaValidator.checkSizeQuota(player, cuboid);
            if (!sizeCheck.success()) return sizeCheck;
        }

        session.setPosition(position, blockPos);
        return ActionResult.ok(locale);
    }

    public ActionResult claimRegion(Player player, Cuboid cuboid, String name) {
        ActionResult sizeCheck = this.quotaValidator.checkSizeQuota(player, cuboid);
        if (!sizeCheck.success()) return sizeCheck;

        World world = player.getWorld();

        return this.claimService.claimRegion(player, world, cuboid, name);
    }
}
