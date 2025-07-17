package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.member.Member;
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
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class MembersMenu extends AbstractClaimMenu implements Filled<Member>, ConfigBased, ClaimMenu {

    private String       memberName;
    private List<String> memberLore;
    private int[]        memberSlots;

    public MembersMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, BLACK.wrap(CLAIM_NAME + "'s Members"), ClaimPermission.VIEW_MEMBERS);
    }

    @Override
    @NotNull
    protected SettingsMenu getBackMenu() {
        return this.plugin.getMenuManager().getSettingsMenu();
    }

    @Override
    @NotNull
    public MenuFiller<Member> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(viewer);

        return MenuFiller.builder(this)
            .setSlots(this.memberSlots)
            .setItems(claim.getMembers().stream().sorted(Comparator.comparingInt((Member mem) -> mem.getRank().getPriority()).reversed()).toList())
            .setItemCreator(member -> {
                return new NightItem(Material.PLAYER_HEAD)
                    .setPlayerProfile(member.getOfflinePlayer())
                    .setDisplayName(this.memberName)
                    .setLore(this.memberLore)
                    .replacement(replacer -> replacer
                        .replace(GENERIC_NAME, member::getPlayerName)
                        .replace(member.getRank().replacePlaceholders())
                    );
            })
            .setItemClick(member -> (viewer1, event) -> {
                this.runNextTick(() -> plugin.getMenuManager().openMemberMenu(player, claim, member));
            })
            .build();
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    private void handleMemberAdd(@NotNull MenuViewer viewer) {
        this.runNextTick(() -> plugin.getMenuManager().openAddMemberMenu(viewer.getPlayer(), this.getLink(viewer)));
    }

    private void handleMembersClear(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);

        this.runNextTick(() -> UIUtils.openConfirmation(player, Confirmation.builder()
            .onAccept((viewer1, event) -> {
                claim.getMemberMap().clear();
                claim.setSaveRequired(true);
            })
            .onReturn((viewer1, event) -> this.runNextTick(() -> this.open(player, claim)))
            .returnOnAccept(true)
            .build()));
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.memberName = ConfigValue.create("Member.Name",
            LIGHT_YELLOW.wrap(BOLD.wrap(GENERIC_NAME))
        ).read(config);

        this.memberLore = ConfigValue.create("Member.Lore", Lists.newList(
            LIGHT_GRAY.wrap("Rank: " + LIGHT_YELLOW.wrap(RANK_NAME)),
            EMPTY_IF_BELOW,
            RANK_PERMISSIONS,
            EMPTY_IF_BELOW,
            LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to open"))
        )).read(config);

        this.memberSlots = ConfigValue.create("Member.Slots", new int[]{10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34}).read(config);

        loader.addDefaultItem(NightItem.fromType(Material.SPYGLASS)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Add Member")))
            .setLore(Lists.newList(
                GRAY.wrap("Add new member to the claim."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to add"))
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(52)
            .setHandler(new ItemHandler("add_member", (viewer, event) -> this.handleMemberAdd(viewer),
                ItemOptions.builder()
                    .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.ADD_MEMBERS))
                    .build())
            )
        );

        loader.addDefaultItem(NightItem.fromType(Material.LAVA_BUCKET)
            .setDisplayName(LIGHT_ORANGE.wrap(BOLD.wrap("Clear Members")))
            .setLore(Lists.newList(
                GRAY.wrap("Remove all members from the claim."),
                "",
                LIGHT_ORANGE.wrap("→ " + UNDERLINED.wrap("Click to clear"))
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(46)
            .setHandler(new ItemHandler("clear_members", (viewer, event) -> this.handleMembersClear(viewer),
                ItemOptions.builder()
                    .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.REMOVE_MEMBERS))
                    .build())
            )
        );

        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(45, 54).toArray()));

        loader.addDefaultItem(MenuItem.buildNextPage(this, 26));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 18));
        loader.addDefaultItem(this.createBackButton(49));
    }
}
