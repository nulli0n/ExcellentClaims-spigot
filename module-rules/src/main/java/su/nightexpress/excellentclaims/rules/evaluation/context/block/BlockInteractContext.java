package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockContext;

@NullMarked
public record BlockInteractContext(Player actor,
                                   Block block,
                                   Action action) implements ActionContext, ActorContext, BlockContext {

}
