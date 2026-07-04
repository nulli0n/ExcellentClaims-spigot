package su.nightexpress.excellentclaims.wilderness.rules.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;

public class WildernessRuleUIController {

    private final RegionRuleUIService uiService;
    private final MessageDispatcher   dispatcher;

    public WildernessRuleUIController(RegionRuleUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    public void onRulesClick(Player player, WildernessRegion claim) {
        this.uiService.openRules(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
