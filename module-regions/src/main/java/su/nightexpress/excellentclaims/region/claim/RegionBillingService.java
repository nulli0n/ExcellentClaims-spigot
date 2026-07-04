package su.nightexpress.excellentclaims.region.claim;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public class RegionBillingService {

    private final RegionSettings settings;

    public RegionBillingService(RegionSettings settings) {
        this.settings = settings;
    }

    public ActionResult canAffordClaim(Player player) {
        if (player.hasPermission(RegionPerms.BYPASS_CLAIMING_COST)) return ActionResult.ok();

        String currencyId = this.settings.getBillingCurrency();
        double cost = this.settings.getBillingCreationCost();

        Currency currency = EconomyBridge.api().getCurrency(currencyId);
        if (currency == null) return ActionResult.ok();

        if (!currency.canAfford(player, cost)) {
            return ActionResult.fail(RegionLang.CLAIMING_INSUFFICIENT_FUNDS, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> currency.format(cost))
            );
        }

        return ActionResult.ok();
    }

    public void chargeForClaim(Player player) {
        if (player.hasPermission(RegionPerms.BYPASS_CLAIMING_COST)) return;

        String currency = this.settings.getBillingCurrency();
        double cost = this.settings.getBillingCreationCost();

        EconomyBridge.api().withdraw(player, currency, cost);
    }
}
