package su.nightexpress.excellentclaims.rules.impl.player.projectile;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Projectile;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.ProjectileLaunchContext;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerProjectileLaunchRule;

@NullMarked
public class ShootArrowsRule extends BasePlayerProjectileLaunchRule {

    @Override
    protected boolean shouldHandle(ProjectileLaunchContext context, Projectile projectile) {
        return projectile instanceof Arrow;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Shoot Arrows")
            .description(
                "Controls whether outsiders",
                "can shoot arrows here."
            )
            .icon(Material.ARROW)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
