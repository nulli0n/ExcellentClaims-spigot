package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.BlockNewStateContext;
import su.nightexpress.excellentclaims.api.rule.context.SourceBlockContext;
import su.nightexpress.excellentclaims.api.rule.context.TargetBlockContext;

@NullMarked
public record BlockGrowContext(Block sourceBlock,
                               Block targetBlock,
                               BlockState newState) implements ActionContext, TargetBlockContext, SourceBlockContext, BlockNewStateContext {

    @Override
    public @Nullable Player actor() {
        return null;
    }
}
