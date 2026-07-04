package su.nightexpress.excellentclaims.rules.impl.player.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseEntityRule;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseVillagersRule extends BasePlayerUseEntityRule {

    public UseVillagersRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractAtEntityEvent event, Entity entity) {
        return entity instanceof Villager;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Villagers")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Entity Interact") + " permission",
                "can interact with villagers."
            )
            .icon(NightItem.asCustomHead("a36e9841794a37eb99524925668b47a62b5cb72e096a9f8f95e106804ae13e1b"))
            .build();
    }
}
