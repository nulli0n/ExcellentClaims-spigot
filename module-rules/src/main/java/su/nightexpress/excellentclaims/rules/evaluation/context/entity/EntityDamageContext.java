package su.nightexpress.excellentclaims.rules.evaluation.context.entity;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.EntityContext;

@NullMarked
public record EntityDamageContext(@Nullable Player actor,
                                  Entity entity,
                                  DamageSource damageSource) implements ActionContext, ActorContext, EntityContext {

}
