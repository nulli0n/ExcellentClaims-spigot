package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.rules.impl.entity.block.AnimalGriefRule;
import su.nightexpress.excellentclaims.rules.impl.entity.block.EnderDragonGriefRule;
import su.nightexpress.excellentclaims.rules.impl.entity.block.EndermanGriefRule;
import su.nightexpress.excellentclaims.rules.impl.entity.block.RavagerGriefRule;
import su.nightexpress.excellentclaims.rules.impl.entity.block.SilverfishInfestRule;
import su.nightexpress.excellentclaims.rules.impl.entity.block.SnowmanTrailRule;
import su.nightexpress.excellentclaims.rules.impl.entity.block.VillagerFarmRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class MobGriefRulesRegistrar {

    private MobGriefRulesRegistrar() {
    }

    public static void register(RuleLoader loader, ClaimPermissionAPI permissions) {
        loader.addRuleSpec("animal_grief", new AnimalGriefRule(permissions));
        loader.addRuleSpec("villager_farm", new VillagerFarmRule(permissions));
        loader.addRuleSpec("silverfish_infest", new SilverfishInfestRule(permissions));
        loader.addRuleSpec("enderman_grief", new EndermanGriefRule(permissions));
        loader.addRuleSpec("ender_dragon_grief", new EnderDragonGriefRule(permissions));
        loader.addRuleSpec("ravager_grief", new RavagerGriefRule(permissions));
        loader.addRuleSpec("snowman_trail", new SnowmanTrailRule(permissions));
    }
}
