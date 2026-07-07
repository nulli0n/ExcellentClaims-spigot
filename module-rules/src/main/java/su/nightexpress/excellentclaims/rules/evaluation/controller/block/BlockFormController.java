package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFormEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFormContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

@NullMarked
public class BlockFormController extends AbstractEventController {

    public BlockFormController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockForm(BlockFormEvent event) {
        Block block = event.getBlock();
        BlockState newState = event.getNewState();

        BlockFormContext context = new BlockFormContext(block, newState);
        RuleResult result = this.engine.evaluate(context);
        if (result.state() == EventState.DENY) {
            event.setCancelled(true);
        }
    }
}
