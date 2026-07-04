package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.entity.explode.CreeperBlockDamageRule;
import su.nightexpress.excellentclaims.rules.impl.entity.explode.EndCrystalBlockDamage;
import su.nightexpress.excellentclaims.rules.impl.entity.explode.FireballBlockDamage;
import su.nightexpress.excellentclaims.rules.impl.entity.explode.TNTBlockDamageRule;
import su.nightexpress.excellentclaims.rules.impl.entity.explode.WitherBlockDamageRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class MobDamageRulesRegistrar {

    private MobDamageRulesRegistrar() {
    }

    public static void register(RuleLoader loader) {
        loader.addRuleSpec("tnt_block_damage", new TNTBlockDamageRule());
        loader.addRuleSpec("creeper_block_damage", new CreeperBlockDamageRule());
        loader.addRuleSpec("wither_block_damage", new WitherBlockDamageRule());
        loader.addRuleSpec("fireball_block_damage", new FireballBlockDamage());
        loader.addRuleSpec("end_crystal_block_damage", new EndCrystalBlockDamage());
    }
}
