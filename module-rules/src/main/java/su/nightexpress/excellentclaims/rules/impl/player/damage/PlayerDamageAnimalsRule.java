package su.nightexpress.excellentclaims.rules.impl.player.damage;

import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerDamageEntityRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PlayerDamageAnimalsRule extends BasePlayerDamageEntityRule {

    public PlayerDamageAnimalsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(EntityDamageContext context, Entity entity) {
        return entity instanceof Animals;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Player Damage Animals")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Damage Mobs") + " permission",
                "can damage animals here."
            )
            .icon(Material.PIG_SPAWN_EGG)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
