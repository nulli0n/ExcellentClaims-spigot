package su.nightexpress.excellentclaims.rules.impl.entity.spawn;

import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.CreatureSpawnContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntitySpawnRule;

@NullMarked
public class SpawnAnimalsRule extends BaseEntitySpawnRule {

    @Override
    protected boolean shouldHandle(CreatureSpawnContext context, LivingEntity entity) {
        return entity instanceof Animals;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Spawn Animals")
            .description(
                "Controls whether animals",
                "can spawn here."
            )
            .icon(Material.PIG_SPAWN_EGG)
            .build();
    }
}
