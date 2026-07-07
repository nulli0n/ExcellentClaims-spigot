package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.block.Block;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockBreakContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityRemoveContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class BlockEntityBreakController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public BlockEntityBreakController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockBreakContext context = new BlockBreakContext(player, block);

        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_DESTROYING, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
            );
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        DamageSource source = event.getDamageSource();
        if (!(source.getCausingEntity() instanceof Player player)) return;

        Entity entity = event.getEntity();

        if (this.handleEntityRemove(player, entity) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        DamageSource source = event.getDamageSource();
        if (!(source.getCausingEntity() instanceof Player player)) return;

        Entity entity = event.getVehicle();

        if (this.handleEntityRemove(player, entity) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    private EventState handleEntityRemove(Player player, Entity entity) {
        EntityRemoveContext context = new EntityRemoveContext(player, entity);
        EventState state = this.engine.evaluate(context).state();

        if (this.engine.evaluate(context).state() == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_DESTROYING, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(entity.getType()))
            );
        }

        return state;
    }
}
