package su.nightexpress.excellentclaims.selection.listener;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.event.chunk.ChunkClaimedEvent;
import su.nightexpress.excellentclaims.api.event.chunk.ChunkUnclaimedEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionCreatedEvent;
import su.nightexpress.excellentclaims.api.event.region.RegionRemovedEvent;
import su.nightexpress.excellentclaims.selection.SelectionManager;
import su.nightexpress.excellentclaims.selection.type.ItemType;
import su.nightexpress.nightcore.manager.AbstractListener;

import java.util.stream.Stream;

public class SelectionListener extends AbstractListener<ClaimPlugin> {

    private final SelectionManager manager;

    public SelectionListener(@NotNull ClaimPlugin plugin, @NotNull SelectionManager manager) {
        super(plugin);
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkClaim(ChunkClaimedEvent event) {
        this.manager.resetTrackedChunks();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegionCreate(RegionCreatedEvent event) {
        this.manager.resetTrackedChunks();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChunkUnclaim(ChunkUnclaimedEvent event) {
        this.manager.resetTrackedChunks();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRegionRemove(RegionRemovedEvent event) {
        this.manager.resetTrackedChunks();
    }



    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        this.manager.removeAll(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        this.manager.removeAll(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null || block.getType().isAir()) return;

        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;

        ItemType itemType = this.manager.getItemType(itemStack);
        if (itemType == null) return;

        event.setUseInteractedBlock(Event.Result.DENY);
        event.setUseItemInHand(Event.Result.DENY);

        Player player = event.getPlayer();
        this.manager.onItemUse(player, itemType, block, event.getAction());
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        ItemType itemType = this.manager.getItemType(item);
        if (itemType == null) return;

        item.setAmount(0);
        event.setCancelled(true);
        this.manager.onItemDrop(player, itemType);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemMove(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && this.manager.isItem(item)) {
            Inventory inventory = event.getInventory();
            if (inventory.getType() != InventoryType.CRAFTING) {
                event.setCancelled(true);
                return;
            }
        }

        int hotkey = event.getHotbarButton();
        if (hotkey >= 0) {
            Player player = (Player) event.getWhoClicked();
            ItemStack hotItem = player.getInventory().getItem(hotkey);
            if (hotItem != null && this.manager.isItem(hotItem)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemCraft(PrepareItemCraftEvent event) {
        if (Stream.of(event.getInventory().getMatrix()).anyMatch(item -> item != null && this.manager.isItem(item))) {
            event.getInventory().setResult(null);
        }
    }
}
