package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockStateContext;

@NullMarked
public record BlockFertilizeContext(Player actor,
                                    BlockState blockState) implements ActionContext, ActorContext, BlockStateContext {

}
