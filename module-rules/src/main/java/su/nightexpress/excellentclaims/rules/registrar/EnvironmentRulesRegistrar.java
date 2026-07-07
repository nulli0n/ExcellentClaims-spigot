package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.environment.block.ExplosionDamageBlocksRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.FireDamageBlocksRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.fade.LeavesDecayRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class EnvironmentRulesRegistrar {

    private EnvironmentRulesRegistrar() {
    }

    public static void register(RuleLoader loader) {
        loader.addRuleSpec("explosion_damage_blocks", new ExplosionDamageBlocksRule());
        loader.addRuleSpec("fire_damage_blocks", new FireDamageBlocksRule());
        loader.addRuleSpec("leaves_decay", new LeavesDecayRule());
    }
}
