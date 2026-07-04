package su.nightexpress.excellentclaims.wilderness.rules.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.wilderness.data.WildernessDataService;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;

public class RegionRuleUIService {

    private final RulesAPI              rulesAPI;
    private final WildernessUIService   coreUI;
    private final WildernessDataService dataService;

    public RegionRuleUIService(RulesAPI rulesAPI, WildernessUIService coreUI, WildernessDataService dataService) {
        this.rulesAPI = rulesAPI;
        this.coreUI = coreUI;
        this.dataService = dataService;
    }

    public ActionResult canOpenRules(Player player, WildernessRegion claim) {
        return this.rulesAPI.canOpenRules(player, claim);
    }

    public ActionResult openRules(Player player, WildernessRegion claim) {
        Runnable callback = () -> this.coreUI.showClaimMenu(player, claim);

        return this.rulesAPI.openRulesMenu(player, claim, this.dataService, null, callback);
    }
}
