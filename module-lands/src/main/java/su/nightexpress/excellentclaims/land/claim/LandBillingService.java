package su.nightexpress.excellentclaims.land.claim;

import org.bukkit.entity.Player;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.bridge.currency.Currency;
import su.nightexpress.nightcore.integration.currency.EconomyBridge;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public class LandBillingService {

    private final LandSettings settings;

    public LandBillingService(LandSettings settings) {
        this.settings = settings;
    }

    public ActionResult canAffordClaim(Player player) {
        if (player.hasPermission(LandPerms.BYPASS_CLAIMING_COST)) return ActionResult.ok();

        String currencyId = this.settings.getBillingCurrency();
        double cost = this.settings.getBillingChunkClaimCost();

        Currency currency = EconomyBridge.api().getCurrency(currencyId);
        if (currency == null) return ActionResult.ok();

        if (!currency.canAfford(player, cost)) {
            return ActionResult.fail(LandLang.CLAIMING_INSUFFICIENT_FUNDS, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> currency.format(cost))
            );
        }

        return ActionResult.ok();
    }

    public void chargeForClaim(Player player) {
        if (player.hasPermission(LandPerms.BYPASS_CLAIMING_COST)) return;

        String currency = this.settings.getBillingCurrency();
        double cost = this.settings.getBillingChunkClaimCost();

        EconomyBridge.api().withdraw(player, currency, cost);
    }
}
