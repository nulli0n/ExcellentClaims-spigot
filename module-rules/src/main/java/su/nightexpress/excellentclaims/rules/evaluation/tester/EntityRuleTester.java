package su.nightexpress.excellentclaims.rules.evaluation.tester;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.tester.EntityTester;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.CreatureSpawnContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;

@NullMarked
public class EntityRuleTester implements EntityTester {

    private final EvaluatorEngine engine;

    public EntityRuleTester(EvaluatorEngine engine) {
        this.engine = engine;
    }

    @Override
    public boolean canChange(Entity entity, Block block) {
        EntityChangeBlockContext context = new EntityChangeBlockContext(null, entity, block);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canDamage(Entity entity, DamageSource source) {
        EntityDamageContext context = new EntityDamageContext(null, entity, source);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canSpawn(LivingEntity entity, Location location) {
        CreatureSpawnContext context = new CreatureSpawnContext(entity, location);
        return this.engine.quickTest(context);
    }
}
