package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.nightcore.util.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class FlagsTypeMenu extends ConfigMenu<ClaimPlugin> implements Linked<Claim> {

    public static final String FILE_NAME = "flag_types.yml";

    private final ViewLink<Claim> link;
    private final ItemHandler returnHandler;
    private final ItemHandler naturalHandler;
    private final ItemHandler playerHandler;
    private final ItemHandler entityHandler;

    public FlagsTypeMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addHandler(this.naturalHandler = new ItemHandler(FlagCategory.NATURAL.name(), (viewer, event) -> {
            this.onCategoryClick(viewer, FlagCategory.NATURAL);
        }));

        this.addHandler(this.playerHandler = new ItemHandler(FlagCategory.PLAYER.name(), (viewer, event) -> {
            this.onCategoryClick(viewer, FlagCategory.PLAYER);
        }));

        this.addHandler(this.entityHandler = new ItemHandler(FlagCategory.ENTITY.name(), (viewer, event) -> {
            this.onCategoryClick(viewer, FlagCategory.ENTITY);
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            ItemHandler handler = menuItem.getHandler();
            FlagCategory category;
            if (handler == this.naturalHandler) category = FlagCategory.NATURAL;
            else if (handler == this.playerHandler) category = FlagCategory.PLAYER;
            else if (handler == this.entityHandler) category = FlagCategory.ENTITY;
            else return;

            menuItem.getOptions().setVisibilityPolicy(viewer -> viewer.getPlayer().hasPermission(Perms.FLAG_TYPE.apply(category)));
        });
    }

    @Override
    @NotNull
    public ViewLink<Claim> getLink() {
        return this.link;
    }

    private void onCategoryClick(@NotNull MenuViewer viewer, @NotNull FlagCategory category) {
        this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(viewer.getPlayer(), this.getLink(viewer), category));
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
        return new MenuOptions(BLACK.enclose("Select category..."), MenuSize.CHEST_36);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack returnItem = ItemUtil.getSkinHead(SKIN_ARROW_DOWN);
        ItemUtil.editMeta(returnItem, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_RETURN.getLocalizedName());
        });
        list.add(new MenuItem(returnItem).setPriority(10).setSlots(31).setHandler(this.returnHandler));

        ItemStack naturalItem = ItemUtil.getSkinHead("89f7a04ac334fcaf618da9e841f03c00d749002dc592f8540ef9534442cecf42");
        ItemUtil.editMeta(naturalItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Natural Flags")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Flags to control natural"),
                LIGHT_GRAY.enclose("world behavior inside a claim."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")
            ));
        });
        list.add(new MenuItem(naturalItem).setPriority(10).setSlots(11).setHandler(this.naturalHandler));

        ItemStack playerItem = ItemUtil.getSkinHead("97e6e5657b8f85f3af2c835b3533856607682f8571a4548967e2bdb535ac56b7");
        ItemUtil.editMeta(playerItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Player Flags")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Flags to control player and"),
                LIGHT_GRAY.enclose("non-member activity inside a claim."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")
            ));
        });
        list.add(new MenuItem(playerItem).setPriority(10).setSlots(13).setHandler(this.playerHandler));

        ItemStack entityItem = ItemUtil.getSkinHead("1c26ec209756ff5d5b81f25ca4db2ee4dceb52874e5f35bb98ce82cace8ac506");
        ItemUtil.editMeta(entityItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Entity Flags")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Flags to control entity activity"),
                LIGHT_GRAY.enclose("and behavior inside a claim."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")
            ));
        });
        list.add(new MenuItem(entityItem).setPriority(10).setSlots(15).setHandler(this.entityHandler));

        return list;
    }

    @Override
    protected void loadAdditional() {

    }
}
