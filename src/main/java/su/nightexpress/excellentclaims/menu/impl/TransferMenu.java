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
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.CLAIM_NAME;
import static su.nightexpress.excellentclaims.Placeholders.PLAYER_NAME;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@SuppressWarnings("UnstableApiUsage")
public class TransferMenu extends LinkedMenu<ClaimPlugin, Claim> implements Filled<Player>, ConfigBased {

    public static final String FILE_NAME = "claim_transfer.yml";

    private String       playerName;
    private List<String> playerLore;
    private int[]        playerSlots;

    public TransferMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, BLACK.enclose("Transfer Claim: " + CLAIM_NAME));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        return this.getLink(viewer).replacePlaceholders().apply(this.title);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public @NotNull MenuFiller<Player> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);
        Set<Player> players = new HashSet<>(plugin.getServer().getOnlinePlayers());
        players.remove(player);

        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(this.playerSlots);
        autoFill.setItems(players.stream().sorted(Comparator.comparing(Player::getName)).toList());
        autoFill.setItemCreator(target -> {
            return new NightItem(Material.PLAYER_HEAD)
                .setSkullOwner(target)
                .setDisplayName(this.playerName)
                .setLore(this.playerLore)
                .replacement(replacer -> replacer.replace(Placeholders.forPlayer(target)));
        });
        autoFill.setItemClick(target -> (viewer1, event) -> {
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

        return autoFill.build();
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.playerName = ConfigValue.create("Player.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(PLAYER_NAME))
        ).read(config);

        this.playerLore = ConfigValue.create("Player.Lore", Lists.newList(
            "",
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("transfer ownership") + ".")
        )).read(config);

        this.playerSlots = ConfigValue.create("Player.Slots", IntStream.range(0, 36).toArray()).read(config);

        loader.addDefaultItem(MenuItem.buildNextPage(this, 44));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 36));
        loader.addDefaultItem(MenuItem.buildReturn(this, 40, this.manageLink((viewer, event, claim) -> {
            this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(viewer.getPlayer(), claim));
        }), ItemOptions.builder()
            .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_CLAIM))
            .build())
        );
    }
}
