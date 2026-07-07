package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityInteractContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemInteractContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class PlayerInteractController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public PlayerInteractController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onEntityInteract(EntityInteractEvent event) {
        Block block = event.getBlock();
        Entity entity = event.getEntity();
        Player actor = null;

        if (entity instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            actor = player;
        }

        if (actor != null) {
            if (this.handleBlockInteraction(actor, block, Action.RIGHT_CLICK_BLOCK) == EventState.DENY) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        ItemStack itemStack = event.getItem();
        Action action = event.getAction();

        if (block != null && !block.isEmpty() && event.useInteractedBlock() != Result.DENY) {
            if (this.handleBlockInteraction(player, block, action) == EventState.DENY) {
                event.setUseInteractedBlock(Result.DENY);
            }
        }

        if (itemStack != null && !itemStack.getType().isAir() && event.useItemInHand() != Result.DENY) {
            ItemInteractContext context = new ItemInteractContext(player, itemStack);
            if (this.handleItemInteraction(context) == EventState.DENY) {
                event.setUseItemInHand(Result.DENY);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerEntityInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        EntityInteractContext context = new EntityInteractContext(player, entity);
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            event.setCancelled(true);

            this.dispatcher.send(RulesLang.PROTECTION_ENTITY_INTERACT, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(entity.getType()))
            );
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (this.handleBlockInteraction(player, block, Action.RIGHT_CLICK_BLOCK) == EventState.DENY) {
            event.setCancelled(true);
        }
    }

    private EventState handleBlockInteraction(Player player, Block block, Action action) {
        BlockInteractContext context = new BlockInteractContext(player, block, action);
        EventState state = this.engine.evaluate(context).state();

        if (state == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_BLOCK_INTERACT, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
            );
        }

        return state;
    }

    private EventState handleItemInteraction(ItemInteractContext context) {
        Player player = context.actor();
        ItemStack itemStack = context.itemStack();
        EventState state = this.engine.evaluate(context).state();

        if (state == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_ITEM_USE, player, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(itemStack.getType()))
            );
        }

        return state;
    }
}
