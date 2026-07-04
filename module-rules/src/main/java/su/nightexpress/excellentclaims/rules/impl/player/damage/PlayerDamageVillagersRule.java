package su.nightexpress.excellentclaims.rules.impl.player.damage;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerDamageEntityRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PlayerDamageVillagersRule extends BasePlayerDamageEntityRule {

    public PlayerDamageVillagersRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityDamageByEntityEvent event, Entity entity) {
        return entity instanceof Villager;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Player Damage Villagers")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Damage Mobs") + " permission",
                "can damage villagers here."
            )
            .icon(Material.VILLAGER_SPAWN_EGG)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
