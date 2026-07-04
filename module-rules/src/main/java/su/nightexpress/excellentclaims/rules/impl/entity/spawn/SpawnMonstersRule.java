package su.nightexpress.excellentclaims.rules.impl.entity.spawn;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntitySpawnRule;

@NullMarked
public class SpawnMonstersRule extends BaseEntitySpawnRule {

    @Override
    protected boolean shouldHandle(CreatureSpawnEvent event, LivingEntity entity) {
        return entity instanceof Monster;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Spawn Monsters")
            .description(
                "Controls whether monsters",
                "can spawn here."
            )
            .icon(Material.ZOMBIE_HEAD)
            .build();
    }
}
