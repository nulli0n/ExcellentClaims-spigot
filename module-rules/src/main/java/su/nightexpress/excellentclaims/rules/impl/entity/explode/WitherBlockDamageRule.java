package su.nightexpress.excellentclaims.rules.impl.entity.explode;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityExplodeBlocksRule;

@NullMarked
public class WitherBlockDamageRule extends BaseEntityExplodeBlocksRule {

    @Override
    protected boolean shouldHandle(EntityExplodeEvent event, Entity entity) {
        return entity instanceof Wither || entity instanceof WitherSkull;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Wither Damage")
            .description(
                "Controls whether Withers",
                "can damage blocks here."
            )
            .icon(Material.WITHER_SKELETON_SKULL)
            .build();
    }
}
