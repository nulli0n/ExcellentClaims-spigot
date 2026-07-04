package su.nightexpress.excellentclaims.land.rules.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.data.LandDataService;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.ui.LandUIService;

public class LandRuleUIService {

    private final RulesAPI        rulesAPI;
    private final LandUIService   coreUI;
    private final LandDataService dataService;

    public LandRuleUIService(RulesAPI rulesAPI, LandUIService coreUI, LandDataService dataService) {
        this.rulesAPI = rulesAPI;
        this.coreUI = coreUI;
        this.dataService = dataService;
    }

    public ActionResult canOpenRules(Player player, LandClaim claim) {
        return this.rulesAPI.canOpenRules(player, claim);
    }

    public ActionResult openRules(Player player, LandClaim claim) {
        Runnable callback = () -> this.coreUI.showClaimMenu(player, claim);

        return this.rulesAPI.openRulesMenu(player, claim, this.dataService, null, callback);
    }
}
