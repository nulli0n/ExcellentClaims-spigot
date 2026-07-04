package su.nightexpress.excellentclaims.land.rules.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;

public class LandRuleUIController {

    private final LandRuleUIService uiService;
    private final MessageDispatcher dispatcher;

    public LandRuleUIController(LandRuleUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onRulesClick(Player player, LandClaim claim) {
        this.uiService.openRules(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
