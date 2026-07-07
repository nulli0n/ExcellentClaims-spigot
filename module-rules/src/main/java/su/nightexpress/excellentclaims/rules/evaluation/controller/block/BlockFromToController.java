package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFromToEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFromToContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

@NullMarked
public class BlockFromToController extends AbstractEventController {

    public BlockFromToController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockForm(BlockFromToEvent event) {
        Block block = event.getBlock();
        Block target = event.getToBlock();

        BlockFromToContext context = new BlockFromToContext(block, target);
        RuleResult result = this.engine.evaluate(context);
        if (result.state() == EventState.DENY) {
            event.setCancelled(true);
        }
    }
}
