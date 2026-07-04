package su.nightexpress.excellentclaims.rules.impl.entity.spawn;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WaterMob;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntitySpawnRule;

@NullMarked
public class SpawnFishesRule extends BaseEntitySpawnRule {

    @Override
    protected boolean shouldHandle(CreatureSpawnEvent event, LivingEntity entity) {
        return entity instanceof WaterMob;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Spawn Water Mobs")
            .description(
                "Controls whether water",
                "mobs can spawn here."
            )
            .icon(Material.COD)
            .build();
    }
}
