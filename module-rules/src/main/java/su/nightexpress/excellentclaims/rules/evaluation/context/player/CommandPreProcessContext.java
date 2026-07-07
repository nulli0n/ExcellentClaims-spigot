package su.nightexpress.excellentclaims.rules.evaluation.context.player;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;

@NullMarked
public record CommandPreProcessContext(Player actor, Command command) implements ActionContext, ActorContext {

}
