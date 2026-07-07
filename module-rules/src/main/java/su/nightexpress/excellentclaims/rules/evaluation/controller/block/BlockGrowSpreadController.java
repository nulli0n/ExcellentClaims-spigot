package su.nightexpress.excellentclaims.rules.evaluation.controller.block;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.EventController;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;

@NullMarked
public class BlockGrowSpreadController implements EventController {

    private final EvaluatorEngine engine;
    private final boolean         resetBlockAge;

    private final Map<Material, BlockFace> sourceDirections;
    private final Set<Material>            preventAgeReset;

    public BlockGrowSpreadController(EvaluatorEngine engine, boolean resetBlockAge) {
        this.engine = engine;
        this.resetBlockAge = resetBlockAge;

        this.sourceDirections = new EnumMap<>(Material.class);
        this.preventAgeReset = EnumSet.noneOf(Material.class);

        this.createStrategies();
    }

    private void createStrategies() {
        this.sourceDirections.put(Material.CACTUS, BlockFace.DOWN);
        this.sourceDirections.put(Material.CACTUS_FLOWER, BlockFace.DOWN);
        this.sourceDirections.put(Material.SUGAR_CANE, BlockFace.DOWN);

        this.preventAgeReset.add(Material.KELP);
        this.preventAgeReset.add(Material.CAVE_VINES);
        this.preventAgeReset.add(Material.TWISTING_VINES);
        this.preventAgeReset.add(Material.VINE);
        this.preventAgeReset.add(Material.WEEPING_VINES);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockGrowAsSpread(BlockSpreadEvent event) {
        BlockState newState = event.getNewState();
        Block target = event.getBlock();

        Block source = event.getSource();
        BlockSpreadContext spreadContext = new BlockSpreadContext(source, target, newState);

        RuleResult spreadResult = this.engine.evaluate(spreadContext);
        if (spreadResult.state() == EventState.DENY) {
            event.setCancelled(true);
            return;
        }

        if (spreadResult.state() == EventState.ALLOW) return;

        BlockGrowContext context = new BlockGrowContext(source, target, newState);
        RuleResult result = this.engine.evaluate(context);
        if (result.state() == EventState.DENY) {
            this.applyDenial(event, source);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        BlockState newState = event.getNewState();
        Material newType = newState.getType();
        Block target = event.getBlock();

        BlockFace sourceDirection = this.sourceDirections.getOrDefault(newType, BlockFace.SELF);
        Block source = sourceDirection == BlockFace.SELF ? target : target.getRelative(sourceDirection);
        BlockGrowContext context = new BlockGrowContext(source, target, newState);

        RuleResult result = this.engine.evaluate(context);
        if (result.state() == EventState.DENY) {
            this.applyDenial(event, source);
        }
    }

    private void applyDenial(Cancellable event, Block source) {
        event.setCancelled(true);

        if (this.resetBlockAge && !this.preventAgeReset.contains(source.getType()) && source
            .getBlockData() instanceof Ageable ageable) {

            ageable.setAge(0);
            source.setBlockData(ageable);
        }
    }
}