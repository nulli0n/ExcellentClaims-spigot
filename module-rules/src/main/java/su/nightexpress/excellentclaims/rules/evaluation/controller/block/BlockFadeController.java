package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFadeContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

@NullMarked
public class BlockFadeController extends AbstractEventController {

    public BlockFadeController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockForm(BlockFadeEvent event) {
        Block block = event.getBlock();
        BlockState newState = event.getNewState();

        if (this.handleBlockFade(block, newState) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        Block block = event.getBlock();
        BlockState newState = Material.AIR.createBlockData().createBlockState();

        if (this.handleBlockFade(block, newState) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    private EventState handleBlockFade(Block block, BlockState newState) {
        BlockFadeContext context = new BlockFadeContext(block, newState);
        return this.engine.evaluate(context).state();
    }
}
