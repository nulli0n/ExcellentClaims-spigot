package su.nightexpress.excellentclaims.land.ownership.ui;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.ownership.LandOwnershipService;
import su.nightexpress.excellentclaims.land.ownership.OwnershipLang;
import su.nightexpress.excellentclaims.land.ownership.ui.context.TransferTargetContext;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class OwnershipUIController {

    private final LandOwnershipService      service;
    private final OwnershipUIService        uiService;
    private final OwnershipUIContextFactory contextFactory;
    private final MessageDispatcher         dispatcher;

    public OwnershipUIController(LandOwnershipService service,
                                 OwnershipUIService uiService,
                                 OwnershipUIContextFactory contextFactory,
                                 MessageDispatcher dispatcher) {
        this.service = service;
        this.uiService = uiService;
        this.contextFactory = contextFactory;
        this.dispatcher = dispatcher;
    }

    public void onTransferClick(Player player, LandClaim claim) {
        ActionResult check = this.service.canTransferOwnership(player, claim);
        if (!check.success()) {
            check.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
            return;
        }

        TransferTargetContext targetContext = this.contextFactory.createTargetContext(player, claim);
        if (targetContext.eligibles().isEmpty()) {
            this.dispatcher.send(OwnershipLang.TRANSFER_NO_ELIGIBLES, player);
            return;
        }

        this.uiService.showTransferTargetSelectDialog(player, targetContext);
    }

    public void onTransferTargetSelect(Player player, LandClaim claim, UUID playerId) {
        Player target = Players.getPlayer(playerId);
        if (target == null) {
            this.dispatcher.send(OwnershipLang.TRANSFER_TARGET_OFFLINE, player);
            return;
        }

        ActionResult result = this.service.transferOwnership(player, claim, target);
        if (result.success()) {
            player.closeInventory(); // Force close Claim GUI if successful transfer

            this.dispatcher.send(OwnershipLang.TRANSFER_NOTIFY, target, ctx -> ctx
                .with(claim.placeholders())
                .with(CommonPlaceholders.PLAYER.resolver(player))
            );
        }

        result.handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
