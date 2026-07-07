package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockExplodeContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

@NullMarked
public class BlockExplodeController extends AbstractEventController {

    public BlockExplodeController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        BlockExplodeContext sourceContext = new BlockExplodeContext(event.getBlock());
        if (this.engine.evaluate(sourceContext).state() == EventState.DENY) {
            event.setCancelled(true);
            return;
        }

        List<Block> blocks = event.blockList();
        blocks.removeIf(block -> {
            BlockExplodeContext context = new BlockExplodeContext(block);
            RuleResult result = this.engine.evaluate(context);

            return result.state() == EventState.DENY;
        });

        if (blocks.isEmpty()) {
            event.setCancelled(true);
        }
    }
}
