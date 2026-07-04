package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.rules.impl.player.block.BlockBreakRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.BlockFertilizeRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.BlockHarvestRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.BlockInteractFilterRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.BlockPlaceRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.BlockTrampRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.ChestAccessRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.ContainerAccessRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseAnvilsRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseBedsRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseButtonsRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseCakesRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseCauldronsRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseDoorsRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UsePlatesRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseSignsRule;
import su.nightexpress.excellentclaims.rules.impl.player.block.UseTripwireRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class PlayerBlockRulesRegistrar {

    private PlayerBlockRulesRegistrar() {
    }

    public static void register(RuleLoader loader, ClaimPermissionAPI permissions) {
        loader.addRuleSpec("block_break", new BlockBreakRule(permissions));
        loader.addRuleSpec("block_place", new BlockPlaceRule(permissions));
        loader.addRuleSpec("block_harvest", new BlockHarvestRule(permissions));
        loader.addRuleSpec("block_fertilize", new BlockFertilizeRule(permissions));
        loader.addRuleSpec("block_tramp", new BlockTrampRule(permissions));
        loader.addRuleSpec("block_interact_filter", new BlockInteractFilterRule(permissions));

        loader.addRuleSpec("chest_access", new ChestAccessRule(permissions));
        loader.addRuleSpec("container_access", new ContainerAccessRule(permissions));

        loader.addRuleSpec("use_anvils", new UseAnvilsRule(permissions));
        loader.addRuleSpec("use_beds", new UseBedsRule(permissions));
        loader.addRuleSpec("use_buttons", new UseButtonsRule(permissions));
        loader.addRuleSpec("use_cakes", new UseCakesRule(permissions));
        loader.addRuleSpec("use_cauldrons", new UseCauldronsRule(permissions));
        loader.addRuleSpec("use_doors", new UseDoorsRule(permissions));
        loader.addRuleSpec("use_plates", new UsePlatesRule(permissions));
        loader.addRuleSpec("use_signs", new UseSignsRule(permissions));
        loader.addRuleSpec("use_tripwire", new UseTripwireRule(permissions));
    }
}
