package su.nightexpress.excellentclaims.rules.registrar;

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

    public static void register(RuleLoader loader) {
        loader.addRuleSpec("animal_grief", new AnimalGriefRule());
        loader.addRuleSpec("villager_farm", new VillagerFarmRule());
        loader.addRuleSpec("silverfish_infest", new SilverfishInfestRule());
        loader.addRuleSpec("enderman_grief", new EndermanGriefRule());
        loader.addRuleSpec("ender_dragon_grief", new EnderDragonGriefRule());
        loader.addRuleSpec("ravager_grief", new RavagerGriefRule());
        loader.addRuleSpec("snowman_trail", new SnowmanTrailRule());
    }
}
