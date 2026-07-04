package su.nightexpress.excellentclaims.region.rules.ui;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.rule.RulesAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.data.RegionDataService;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;

public class RegionRuleUIService {

    private final RulesAPI          rulesAPI;
    private final RegionUIService   coreUI;
    private final RegionDataService dataService;

    public RegionRuleUIService(RulesAPI rulesAPI, RegionUIService coreUI, RegionDataService dataService) {
        this.rulesAPI = rulesAPI;
        this.coreUI = coreUI;
        this.dataService = dataService;
    }

    public ActionResult canOpenRules(Player player, RegionClaim claim) {
        return this.rulesAPI.canOpenRules(player, claim);
    }

    public ActionResult openRules(Player player, RegionClaim claim) {
        Runnable callback = () -> this.coreUI.showClaimMenu(player, claim);

        return this.rulesAPI.openRulesMenu(player, claim, this.dataService, null, callback);
    }
}
