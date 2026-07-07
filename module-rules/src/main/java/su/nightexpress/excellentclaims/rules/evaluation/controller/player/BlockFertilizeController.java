package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFertilizeContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class BlockFertilizeController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public BlockFertilizeController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockFertilize(BlockFertilizeEvent event) {
        Block source = event.getBlock();
        BlockState sourceState = source.getState();

        Player player = event.getPlayer();
        BlockFertilizeContext context = new BlockFertilizeContext(player, sourceState);
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_BLOCK_FERTILIZE, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(sourceState.getType()))
            );
            return;
        }

        List<BlockState> affectedBlocks = event.getBlocks();
        affectedBlocks.removeIf(state -> {
            BlockFertilizeContext blockContext = new BlockFertilizeContext(player, state);
            RuleResult result = this.engine.evaluate(blockContext);

            return result.state() == EventState.DENY;
        });

        if (affectedBlocks.isEmpty()) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_BLOCK_FERTILIZE, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(sourceState.getType()))
            );
        }
    }
}
