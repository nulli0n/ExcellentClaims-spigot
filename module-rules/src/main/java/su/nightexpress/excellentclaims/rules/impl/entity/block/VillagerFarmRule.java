package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class VillagerFarmRule extends BaseEntityBlockChangeRule {

    @Override
    protected boolean shouldHandle(EntityChangeBlockEvent event, Entity entity) {
        return entity instanceof Villager;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Villager Farm")
            .description(
                "Controls whether villagers",
                "can farm crops here."
            )
            .icon(NightItem.asCustomHead("ef15fe4b7623c753393524f557b36ec81258a75daba159b39a6f800f7171a475"))
            .build();
    }
}
