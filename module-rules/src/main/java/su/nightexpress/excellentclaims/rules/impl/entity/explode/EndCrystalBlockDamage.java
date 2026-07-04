package su.nightexpress.excellentclaims.rules.impl.entity.explode;

import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityExplodeBlocksRule;

@NullMarked
public class EndCrystalBlockDamage extends BaseEntityExplodeBlocksRule {

    @Override
    protected boolean shouldHandle(EntityExplodeEvent event, Entity entity) {
        return entity instanceof EnderCrystal;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("End Crystal Block Damage")
            .description(
                "Controls whether End Crystals",
                "can damage blocks here."
            )
            .icon(Material.END_CRYSTAL)
            .build();
    }
}
