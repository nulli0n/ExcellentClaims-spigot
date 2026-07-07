package su.nightexpress.excellentclaims.api.rule.tester;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EntityTester {

    boolean canChange(Entity entity, Block block);

    boolean canDamage(Entity entity, DamageSource source);

    boolean canSpawn(LivingEntity entity, Location location);
}
