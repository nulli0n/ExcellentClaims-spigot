package su.nightexpress.excellentclaims.rules.impl.player.projectile;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.ProjectileLaunchContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerProjectileLaunchRule;

@NullMarked
public class ThrowEggsRule extends BasePlayerProjectileLaunchRule {

    @Override
    protected boolean shouldHandle(ProjectileLaunchContext context, Projectile projectile) {
        return projectile.getType() == EntityType.EGG;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Throw Eggs")
            .description(
                "Controls whether outsiders",
                "can throw eggs here."
            )
            .icon(Material.EGG)
            .build();
    }
}
