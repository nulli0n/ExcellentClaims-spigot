package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemUtil;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class ConfirmMenu extends ConfigMenu<ClaimPlugin> implements Linked<Confirmation> {

    public static final String FILE_NAME = "confirmation.yml";

    private final ViewLink<Confirmation> link;

    private final ItemHandler acceptHandler;
    private final ItemHandler cancelHandler;

    public ConfirmMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.acceptHandler = new ItemHandler("accept", (viewer, event) -> {
            this.getLink(viewer).onAccept(viewer, event);
        }));

        this.addHandler(this.cancelHandler = new ItemHandler("decline", (viewer, event) -> {
            this.getLink(viewer).onDecline(viewer, event);
        }));

        this.load();
    }

    @Override
    @NotNull
    public ViewLink<Confirmation> getLink() {
        return this.link;
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Are you sure?"), InventoryType.HOPPER);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack cancelItem = ItemUtil.getSkinHead(SKIN_WRONG_MARK);
        ItemUtil.editMeta(cancelItem, meta -> {
            meta.setDisplayName(LIGHT_RED.enclose(BOLD.enclose("Cancel")));
        });
        list.add(new MenuItem(cancelItem).setPriority(10).setSlots(0).setHandler(this.cancelHandler));

        ItemStack yesItem = ItemUtil.getSkinHead(SKIN_CHECK_MARK);
        ItemUtil.editMeta(yesItem, meta -> {
            meta.setDisplayName(LIGHT_GREEN.enclose(BOLD.enclose("Accept")));
        });
        list.add(new MenuItem(yesItem).setPriority(10).setSlots(4).setHandler(this.acceptHandler));

        return list;
    }

    @Override
    protected void loadAdditional() {

    }
}
