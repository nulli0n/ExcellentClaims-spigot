package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.rules.impl.player.CommandUseFilterRule;
import su.nightexpress.excellentclaims.rules.impl.player.FillBucketRule;
import su.nightexpress.excellentclaims.rules.impl.player.UseEndPortalRule;
import su.nightexpress.excellentclaims.rules.impl.player.UseNetherPortalRule;
import su.nightexpress.excellentclaims.rules.impl.player.item.DropItemsRule;
import su.nightexpress.excellentclaims.rules.impl.player.item.PickupItemsRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

public final class PlayerCommonRulesRegistrar {

    private PlayerCommonRulesRegistrar() {
    }

    public static void register(RuleLoader loader, RulesSettings settings, ClaimPermissionAPI permissions) {
        loader.addRuleSpec("drop_items", new DropItemsRule(permissions));
        loader.addRuleSpec("pickup_items", new PickupItemsRule(permissions));

        loader.addRuleSpec("fill_buckets", new FillBucketRule(permissions));
        //loader.addRuleSpec("empty_buckets", new EmptyBucketRule(permissions));

        loader.addRuleSpec("use_nether_portal", new UseNetherPortalRule(permissions));
        loader.addRuleSpec("use_end_portal", new UseEndPortalRule(permissions));

        loader.addRuleSpec("command_usage_filter", new CommandUseFilterRule(permissions, settings
            .getDefaultCommandBlacklist()));
    }
}
