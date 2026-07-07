package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;

import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.environment.StructureGrowContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;

public class StructureGrowController extends AbstractEventController {

    public StructureGrowController(EvaluatorEngine engine) {
        super(engine);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        Player player = event.getPlayer();

        Location location = event.getLocation();
        BlockState blockState = location.getBlock().getState();
        TreeType species = event.getSpecies();

        StructureGrowContext context = new StructureGrowContext(player, blockState, species);
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);
            return;
        }

        List<BlockState> blocks = event.getBlocks();
        blocks.removeIf(state -> {
            StructureGrowContext blockContext = new StructureGrowContext(player, state, species);
            RuleResult result = this.engine.evaluate(blockContext);

            return result.state() == EventState.DENY;
        });

        if (blocks.isEmpty()) {
            event.setCancelled(true);
        }
    }
}
