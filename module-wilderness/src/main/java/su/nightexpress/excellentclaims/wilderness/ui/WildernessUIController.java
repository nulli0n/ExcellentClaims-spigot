package su.nightexpress.excellentclaims.wilderness.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;

public class WildernessUIController {

    private final WildernessUIService service;
    private final MessageDispatcher   dispatcher;

    public WildernessUIController(WildernessUIService service,
                                  MessageDispatcher dispatcher) {
        this.service = service;
        this.dispatcher = dispatcher;
    }

    public void onClaimListClick(Player player, WildernessRegion claim) {
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
}
