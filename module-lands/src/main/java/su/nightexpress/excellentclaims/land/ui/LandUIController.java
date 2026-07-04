package su.nightexpress.excellentclaims.land.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.ui.context.InspectContext;

public class LandUIController {

    private final LandUIService service;
    //private final LandUIContextFactory      contextFactory;
    private final MessageDispatcher dispatcher;

    public LandUIController(LandUIService service,
                            LandUIContextFactory contextFactory,
                            MessageDispatcher dispatcher) {
        this.service = service;
        //this.contextFactory = contextFactory;
        this.dispatcher = dispatcher;
    }

    public void onClaimListClick(Player player, LandClaim claim) {
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

    public void onClaimInspectClick(Player player, LandClaim claim, InspectContext context) {
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
