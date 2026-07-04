package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Silverfish;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;

@NullMarked
public class SilverfishInfestRule extends BaseEntityBlockChangeRule {

    @Override
    protected boolean shouldHandle(EntityChangeBlockEvent event, Entity entity) {
        return entity instanceof Silverfish;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Silverfish Infest")
            .description(
                "Controls whether silverfishes",
                "can infest blocks here."
            )
            .icon(Material.STONE_BRICKS)
            .build();
    }
}
