package su.nightexpress.excellentclaims.rules.impl.player.projectile;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerProjectileLaunchRule;

@NullMarked
public class ThrowEnderEyesRule extends BasePlayerProjectileLaunchRule {

    @Override
    protected boolean shouldHandle(ProjectileLaunchEvent event, Projectile projectile) {
        return projectile.getType() == EntityType.EYE_OF_ENDER;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Throw Ender Eyes")
            .description(
                "Controls whether outsiders",
                "can throw ender eyes here."
            )
            .icon(Material.ENDER_EYE)
            .build();
    }
}
