package su.nightexpress.excellentclaims.rules.evaluation.context.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.EntityContext;

@NullMarked
public record EntityRemoveContext(Player actor, Entity entity) implements ActionContext, ActorContext, EntityContext {

}
