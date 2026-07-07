package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockContext;

@NullMarked
public record BlockPlaceContext(Player actor,
                                Block block,
                                Material material) implements ActionContext, ActorContext, BlockContext {

}
