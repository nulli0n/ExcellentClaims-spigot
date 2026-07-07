package su.nightexpress.excellentclaims.rules.impl.entity.explode;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityChangeBlockContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityExplodeBlocksRule;

@NullMarked
public class TNTBlockDamageRule extends BaseEntityExplodeBlocksRule {

    @Override
    protected boolean shouldHandle(EntityChangeBlockContext context, Entity entity) {
        return entity instanceof TNTPrimed || entity instanceof ExplosiveMinecart;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("TNT Block Damage")
            .description(
                "Controls whether TNT",
                "can damage blocks here."
            )
            .icon(Material.TNT)
            .build();
    }
}
