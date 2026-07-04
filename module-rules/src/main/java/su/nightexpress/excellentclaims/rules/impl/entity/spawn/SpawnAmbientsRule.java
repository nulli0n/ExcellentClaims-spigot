package su.nightexpress.excellentclaims.rules.impl.entity.spawn;

import org.bukkit.Material;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntitySpawnRule;

@NullMarked
public class SpawnAmbientsRule extends BaseEntitySpawnRule {

    @Override
    protected boolean shouldHandle(CreatureSpawnEvent event, LivingEntity entity) {
        return entity instanceof Ambient;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Spawn Ambients")
            .description(
                "Controls whether ambient",
                "mobs can spawn here."
            )
            .icon(Material.BAT_SPAWN_EGG)
            .build();
    }
}
