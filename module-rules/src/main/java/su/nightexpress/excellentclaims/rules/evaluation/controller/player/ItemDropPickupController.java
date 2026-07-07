package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemDropContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemPickupContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class ItemDropPickupController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public ItemDropPickupController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItemDrop();

        ItemDropContext context = new ItemDropContext(player, item.getItemStack());
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_ITEM_DROP, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(item.getType()))
            );
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        Item item = event.getItem();

        ItemPickupContext context = new ItemPickupContext(player, item.getItemStack());
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_ITEM_PICKUP, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(item.getType()))
            );
        }
    }
}
