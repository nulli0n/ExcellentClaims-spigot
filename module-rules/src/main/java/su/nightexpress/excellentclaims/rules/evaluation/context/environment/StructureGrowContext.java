package su.nightexpress.excellentclaims.rules.evaluation.context.environment;

import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockStateContext;

@NullMarked
public record StructureGrowContext(@Nullable Player actor,
                                   BlockState blockState,
                                   TreeType species) implements ActionContext, ActorContext, BlockStateContext {

}
