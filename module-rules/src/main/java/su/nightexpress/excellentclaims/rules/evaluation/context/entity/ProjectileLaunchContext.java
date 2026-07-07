package su.nightexpress.excellentclaims.rules.evaluation.context.entity;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.ProjectileContext;

@NullMarked
public record ProjectileLaunchContext(@Nullable Player actor,
                                      Projectile projectile) implements ActionContext, ActorContext, ProjectileContext {

}
