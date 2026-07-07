package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockNewStateContext;

@NullMarked
public record BlockFadeContext(Block block,
                               BlockState newState) implements ActionContext, BlockContext, BlockNewStateContext {

}
