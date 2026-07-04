package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseEntityBlockChangeRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class EndermanGriefRule extends BaseEntityBlockChangeRule {

    @Override
    protected boolean shouldHandle(EntityChangeBlockEvent event, Entity entity) {
        return entity instanceof Enderman;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Enderman Grief")
            .description(
                "Controls whether endermans",
                "can pickup blocks here."
            )
            .icon(NightItem.asCustomHead("4f24767c8138b3dfec02f77bd151994d480d4e869664ce09a26b19289212162b"))
            .build();
    }
}
