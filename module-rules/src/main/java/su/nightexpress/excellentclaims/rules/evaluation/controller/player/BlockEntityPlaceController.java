package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockPlaceContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityPlaceContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class BlockEntityPlaceController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public BlockEntityPlaceController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        Material placedType = block.getType();

        if (this.handleBlockPlace(player, block, placedType) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material bucketType = event.getBucket();

        if (this.handleBlockPlace(player, block, bucketType) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityPlace(EntityPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Entity entity = event.getEntity();

        if (this.handleEntityPlace(player, entity, block) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Entity entity = event.getEntity();

        if (this.handleEntityPlace(player, entity, block) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    private EventState handleBlockPlace(Player player, Block block, Material placedType) {
        BlockPlaceContext context = new BlockPlaceContext(player, block, placedType);
        EventState state = this.engine.evaluate(context).state();

        if (state == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_BUILDING, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(placedType))
            );
        }

        return state;
    }

    private EventState handleEntityPlace(Player player, Entity entity, Block block) {
        EntityPlaceContext context = new EntityPlaceContext(player, block, entity);
        EventState state = this.engine.evaluate(context).state();

        if (this.engine.evaluate(context).state() == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_BUILDING, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(entity.getType()))
            );
        }

        return state;
    }
}
