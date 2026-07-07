package su.nightexpress.excellentclaims.rules.evaluation.context.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;

@NullMarked
public record BlockExplodeContext(Block block) implements ActionContext {

    @Override
    public @Nullable Player actor() {
        return null;
    }
}
