package su.nightexpress.excellentclaims.rules.impl.player.entity;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseEntityRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseArmorStandsRule extends BasePlayerUseEntityRule {

    public UseArmorStandsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractAtEntityEvent event, Entity entity) {
        return entity instanceof ArmorStand;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Armor Stands")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Entity Interact") + " permission",
                "can interact with armor stands."
            )
            .icon(Material.ARMOR_STAND)
            .build();
    }
}
