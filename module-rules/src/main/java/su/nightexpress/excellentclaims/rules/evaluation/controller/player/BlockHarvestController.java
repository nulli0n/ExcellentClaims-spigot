package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockHarvestContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

public class BlockHarvestController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public BlockHarvestController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockHarvest(PlayerHarvestBlockEvent event) {
        Player player = event.getPlayer();
        Block block = event.getHarvestedBlock();
        BlockHarvestContext context = new BlockHarvestContext(player, block);

        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_BLOCK_HARVEST, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
            );
        }
    }
}
