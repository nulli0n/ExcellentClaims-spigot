package su.nightexpress.excellentclaims.api.rule.tester;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface EntityTester {

    /**
     * Evaluates whether the specified entity (e.g., Enderman, Silverfish) is permitted to modify the target block.
     * 
     * @param entity
     * @param block
     * @return
     */
    boolean canChange(Entity entity, Block block);

    /**
     * Evaluates whether the specified entity can receive damage from the provided environmental or combat source.
     * 
     * @param entity
     * @param source
     * @return
     */
    boolean canDamage(Entity entity, DamageSource source);

    /**
     * Evaluates whether the specified living entity is permitted to spawn at the target location.
     * 
     * @param entity
     * @param location
     * @return
     */
    boolean canSpawn(LivingEntity entity, Location location);
}
