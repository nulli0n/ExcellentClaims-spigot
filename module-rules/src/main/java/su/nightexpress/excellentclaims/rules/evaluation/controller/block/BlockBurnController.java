package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockBurnContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

public class BlockBurnController extends AbstractEventController {

    public BlockBurnController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        BlockBurnContext context = new BlockBurnContext(event.getBlock());
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);
        }
    }
}
