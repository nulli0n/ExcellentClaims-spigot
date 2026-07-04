package su.nightexpress.excellentclaims.region.selection.session;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.core.AbstractController;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.selection.SelectionPosition;
import su.nightexpress.nightcore.util.geodata.pos.BlockPos;

@NullMarked
public class SessionController extends AbstractController {

    private final SessionService    sessionService;
    private final MessageDispatcher dispatcher;

    public SessionController(ClaimPlugin plugin,
                             SessionService service,
                             MessageDispatcher dispatcher) {
        super(plugin);
        this.sessionService = service;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.sessionService.endSession(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        this.sessionService.endSession(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!this.sessionService.isInSelection(player)) return;

        this.sessionService.toggleSelectionPause(player).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBlockClick(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        if (!this.sessionService.isInSelection(player)) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType().isAir()) return;

        BlockPos blockPos = BlockPos.from(block);
        Action action = event.getAction();
        SelectionPosition position;

        if (action == Action.LEFT_CLICK_BLOCK) {
            position = SelectionPosition.FIRST;
        }
        else if (action == Action.RIGHT_CLICK_BLOCK) {
            position = SelectionPosition.SECOND;
        }
        else return;

        ActionResult result = this.sessionService.selectBlockPosition(player, blockPos, position);
        if (result.success()) {
            event.setUseInteractedBlock(Result.DENY);
            event.setUseItemInHand(Result.DENY);
        }

        result.handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
