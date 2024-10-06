package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;

import java.util.*;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class TransferMenu extends ConfigMenu<ClaimPlugin> implements AutoFilled<Player>, Linked<Claim> {

    public static final String FILE_NAME = "claim_transfer.yml";

    private final ViewLink<Claim> link;
    private final ItemHandler     returnHandler;

    private String       playerName;
    private List<String> playerLore;
    private int[]        playerSlots;

    public TransferMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            if (menuItem.getHandler() == this.returnHandler) {
                menuItem.getOptions().addVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_CLAIM));
            }
        });
    }

    @Override
    @NotNull
    public ViewLink<Claim> getLink() {
        return this.link;
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        options.editTitle(title -> this.getLink(viewer).replacePlaceholders().apply(title));
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Player> autoFill) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);
        Set<Player> players = new HashSet<>(plugin.getServer().getOnlinePlayers());
        players.remove(player);

        autoFill.setSlots(this.playerSlots);
        autoFill.setItems(players.stream().sorted(Comparator.comparing(Player::getName)).toList());
        autoFill.setItemCreator(target -> {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            ItemUtil.editMeta(itemStack, meta -> {
                if (meta instanceof SkullMeta skullMeta) {
                    skullMeta.setOwningPlayer(target);
                }
            });
            ItemReplacer.create(itemStack).trimmed().hideFlags()
                .setDisplayName(this.playerName)
                .setLore(this.playerLore)
                .replace(Placeholders.forPlayer(target))
                .writeMeta();

            return itemStack;
        });
        autoFill.setClickAction(target -> (viewer1, event) -> {
            this.plugin.getMenuManager().openConfirm(player, Confirmation.create(
                (viewer2, event2) -> {
                    this.plugin.getClaimManager().transferOwnership(player, claim, target);
                    this.runNextTick(player::closeInventory);
                },
                (viewer2, event2) -> {
                    this.runNextTick(() -> this.open(player, claim));
                }
            ));
        });
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Transfer Claim: " + CLAIM_NAME), MenuSize.CHEST_45);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack nextPage = ItemUtil.getSkinHead(SKIN_ARROW_RIGHT);
        ItemUtil.editMeta(nextPage, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_NEXT_PAGE.getLocalizedName());
        });
        list.add(new MenuItem(nextPage).setPriority(10).setSlots(44).setHandler(ItemHandler.forNextPage(this)));

        ItemStack backPage = ItemUtil.getSkinHead(SKIN_ARROW_LEFT);
        ItemUtil.editMeta(backPage, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_PREVIOUS_PAGE.getLocalizedName());
        });
        list.add(new MenuItem(backPage).setPriority(10).setSlots(36).setHandler(ItemHandler.forPreviousPage(this)));

        ItemStack returnItem = ItemUtil.getSkinHead(SKIN_ARROW_DOWN);
        ItemUtil.editMeta(returnItem, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_RETURN.getLocalizedName());
        });
        list.add(new MenuItem(returnItem).setPriority(10).setSlots(40).setHandler(this.returnHandler));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.playerName = ConfigValue.create("Player.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(PLAYER_NAME))
        ).read(cfg);

        this.playerLore = ConfigValue.create("Player.Lore", Lists.newList(
            "",
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("transfer ownership") + ".")
        )).read(cfg);

        this.playerSlots = ConfigValue.create("Player.Slots", IntStream.range(0, 36).toArray()).read(cfg);
    }
}
