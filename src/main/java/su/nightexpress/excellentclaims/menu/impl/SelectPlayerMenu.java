package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.menu.type.AbstractClaimMenu;
import su.nightexpress.excellentclaims.menu.type.ClaimMenu;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class SelectPlayerMenu extends AbstractClaimMenu implements Filled<Player>, ConfigBased, ClaimMenu {

    private String       playerName;
    private List<String> playerLore;
    private int[]        playerSlots;

    public SelectPlayerMenu(@NotNull ClaimPlugin plugin, @NotNull ClaimPermission permission) {
        super(plugin, MenuType.GENERIC_9X6, BLACK.wrap("Select Player"), permission);
    }

    @Override
    @NotNull
    protected AbstractClaimMenu getBackMenu() {
        return this.permission == ClaimPermission.ADD_MEMBERS ? this.plugin.getMenuManager().getMembersMenu() : this.plugin.getMenuManager().getSettingsMenu();
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    public MenuFiller<Player> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(viewer);

        Set<Player> players = Players.getOnline();
        if (this.permission == ClaimPermission.ADD_MEMBERS) {
            players.removeIf(claim::isOwnerOrMember);
        }
        else {
            players.removeIf(claim::isOwner);
        }

        return MenuFiller.builder(this)
            .setSlots(this.playerSlots)
            .setItems(players.stream().sorted(Comparator.comparing(Player::getName)).toList())
            .setItemCreator(target -> {
                return new NightItem(Material.PLAYER_HEAD)
                    .setPlayerProfile(target)
                    .setDisplayName(this.playerName)
                    .setLore(this.playerLore)
                    .replacement(replacer -> replacer.replace(Placeholders.forPlayer(target)));
            })
            .setItemClick(target -> (viewer1, event) -> this.handleClick(player, claim, target))
            .build();
    }

    private void handleClick(@NotNull Player player, @NotNull Claim claim, @NotNull Player target) {
        if (this.permission == ClaimPermission.ADD_MEMBERS) {
            claim.addMember(target);
            claim.setSaveRequired(true);
            this.handleReturn(player);
        }
        else if (this.permission == ClaimPermission.TRANSFER_CLAIM) {
            this.runNextTick(() -> UIUtils.openConfirmation(player, Confirmation.builder()
                .onAccept((viewer2, event1) -> {
                    this.plugin.getClaimManager().transferOwnership(player, claim, target);
                    this.runNextTick(player::closeInventory);
                })
                .onReturn((viewer2, event1) -> {
                    this.runNextTick(() -> this.open(player, claim));
                })
                .returnOnAccept(false)
                .build()));
        }
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.playerName = ConfigValue.create("Player.Name",
            LIGHT_YELLOW.wrap(BOLD.wrap(PLAYER_NAME))
        ).read(config);

        this.playerLore = ConfigValue.create("Player.Lore", Lists.newList(
            LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to select"))
        )).read(config);

        this.playerSlots = ConfigValue.create("Player.Slots", new int[]{10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34}).read(config);

        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(45, 54).toArray()));

        loader.addDefaultItem(MenuItem.buildNextPage(this, 26));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 18));
        loader.addDefaultItem(this.createBackButton(49));
    }
}
