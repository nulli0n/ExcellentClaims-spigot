package su.nightexpress.excellentclaims.rules.evaluation.context.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;

@NullMarked
public record EntityChangeBlockContext(@Nullable Player actor,
                                       Entity entity,
                                       Block block) implements ActionContext, ActorContext {

}
