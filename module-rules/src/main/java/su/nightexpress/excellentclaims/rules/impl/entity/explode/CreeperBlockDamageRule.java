package su.nightexpress.excellentclaims.rules.impl.entity.explode;

import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityExplodeBlocksRule;

@NullMarked
public class CreeperBlockDamageRule extends BaseEntityExplodeBlocksRule {

    @Override
    protected boolean shouldHandle(EntityExplodeEvent event, Entity entity) {
        return entity instanceof Creeper;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Creeper Block Damage")
            .description(
                "Controls whether Creepers",
                "can damage blocks here."
            )
            .icon(Material.CREEPER_HEAD)
            .build();
    }
}
