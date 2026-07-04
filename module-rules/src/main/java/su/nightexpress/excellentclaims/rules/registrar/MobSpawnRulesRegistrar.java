package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.entity.spawn.MobSpawnFilterRule;
import su.nightexpress.excellentclaims.rules.impl.entity.spawn.SpawnAmbientsRule;
import su.nightexpress.excellentclaims.rules.impl.entity.spawn.SpawnAnimalsRule;
import su.nightexpress.excellentclaims.rules.impl.entity.spawn.SpawnFishesRule;
import su.nightexpress.excellentclaims.rules.impl.entity.spawn.SpawnMonstersRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class MobSpawnRulesRegistrar {

    private MobSpawnRulesRegistrar() {
    }

    public static void register(RuleLoader loader) {
        loader.addRuleSpec("spawn_ambients", new SpawnAmbientsRule());
        loader.addRuleSpec("spawn_animals", new SpawnAnimalsRule());
        loader.addRuleSpec("spawn_monsters", new SpawnMonstersRule());
        loader.addRuleSpec("spawn_fishes", new SpawnFishesRule());
        loader.addRuleSpec("mob_spawn_filter", new MobSpawnFilterRule());
    }
}
