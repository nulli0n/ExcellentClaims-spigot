package su.nightexpress.excellentclaims.rules.impl.entity.explode;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityExplodeBlocksRule;

@NullMarked
public class FireballBlockDamage extends BaseEntityExplodeBlocksRule {

    @Override
    protected boolean shouldHandle(EntityExplodeEvent event, Entity entity) {
        return entity instanceof Fireball && !(entity instanceof WitherSkull);
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Fireball Block Damage")
            .description(
                "Controls whether Fireballs",
                "can damage blocks here."
            )
            .icon(Material.FIRE_CHARGE)
            .build();
    }
}
