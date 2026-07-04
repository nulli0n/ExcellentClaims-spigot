package su.nightexpress.excellentclaims.region.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.ui.context.InspectContext;

public class RegionUIController {

    private final RegionUIService service;
    //private final LandUIContextFactory      contextFactory;
    private final MessageDispatcher dispatcher;

    public RegionUIController(RegionUIService service,
                              RegionUIContextFactory contextFactory,
                              MessageDispatcher dispatcher) {
        this.service = service;
        //this.contextFactory = contextFactory;
        this.dispatcher = dispatcher;
    }

    public void onClaimListClick(Player player, RegionClaim claim) {
        // Reopen menu if claim was unloaded.
        if (claim.isDisabled()) {
            this.service.showClaimListMenu(player).handleFeedback((locale, ctx) -> {
                this.dispatcher.send(locale, player, ctx);
            });
            return;
        }

        this.service.showClaimMenu(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    public void onClaimInspectClick(Player player, RegionClaim claim, InspectContext context) {
        // Reopen menu if claim was unloaded.
        if (claim.isDisabled()) {
            this.service.showClaimInspectMenu(player, context).handleFeedback((locale, ctx) -> {
                this.dispatcher.send(locale, player, ctx);
            });
            return;
        }

        this.service.showClaimMenu(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
