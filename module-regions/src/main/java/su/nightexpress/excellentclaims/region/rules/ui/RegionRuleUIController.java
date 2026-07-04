package su.nightexpress.excellentclaims.region.rules.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;

public class RegionRuleUIController {

    private final RegionRuleUIService uiService;
    private final MessageDispatcher   dispatcher;

    public RegionRuleUIController(RegionRuleUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onRulesClick(Player player, RegionClaim claim) {
        this.uiService.openRules(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
