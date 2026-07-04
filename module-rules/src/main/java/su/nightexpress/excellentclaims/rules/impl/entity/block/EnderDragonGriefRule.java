package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;

@NullMarked
public class EnderDragonGriefRule extends BaseEntityBlockChangeRule {

    @Override
    protected boolean shouldHandle(EntityChangeBlockEvent event, Entity entity) {
        return entity instanceof EnderDragon;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Ender Dragon Grief")
            .description(
                "Controls whether Ender Dragons",
                "can destroy blocks here."
            )
            .icon(Material.DRAGON_HEAD)
            .build();
    }
}
