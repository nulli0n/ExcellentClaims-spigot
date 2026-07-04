package su.nightexpress.excellentclaims.land.editor.ui.menu;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.editor.ui.LandEditorUIController;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class EditorIconSelectionMenu extends AbstractObjectMenu<LandClaim> {

    private static final String DEFAULT_TITLE = "Put your icon to the empty slot";

    private final LandEditorUIController controller;

    private int iconSlot = 2;

    public EditorIconSelectionMenu(ClaimPlugin plugin, LandEditorUIController controller) {
        super(plugin, MenuType.HOPPER, DEFAULT_TITLE, LandClaim.class);
        this.controller = controller;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, 0, 1, 3, 4);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.HOTBAR_SWAP) return;

        int slot = event.getRawSlot();
        if (slot < event.getInventory().getSize()) {
            if (slot == this.iconSlot) {
                this.handleIconPlacement(context, event.getView(), event.getCursor());
            }
            return;
        }

        // Shift click support
        if (event.isShiftClick()) {
            this.handlePlayerShiftClick(context, event);
            return;
        }

        event.setCancelled(false);
    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {
        Set<Integer> slots = event.getRawSlots();
        if (slots.size() != 1 && !slots.contains(this.iconSlot)) return;

        this.handleIconPlacement(context, event.getView(), event.getOldCursor());
    }

    @Override
    protected void onLoad(FileConfig config) {
        this.iconSlot = config.getOrSet("Icon.Slot", ConfigCodecs.INT, 2);
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {

    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handlePlayerShiftClick(ViewerContext context, InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        Player player = context.getPlayer();
        LandClaim claim = this.getObject(context);
        ItemStack copy = new ItemStack(clickedItem);

        this.plugin.runTask(player, () -> {
            this.controller.onIconSelect(player, claim, copy);
        });
    }

    private void handleIconPlacement(ViewerContext context, InventoryView view, @Nullable ItemStack cursor) {
        if (cursor == null || cursor.getType().isAir()) return;

        Player player = context.getPlayer();
        LandClaim claim = this.getObject(context);
        ItemStack copy = new ItemStack(cursor);

        this.plugin.runTask(player, () -> {
            view.setCursor(null);
            Players.addItem(player, copy);
            this.controller.onIconSelect(player, claim, copy);
        });
    }
}
