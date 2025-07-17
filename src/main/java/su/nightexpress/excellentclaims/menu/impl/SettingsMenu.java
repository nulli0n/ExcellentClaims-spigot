package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.*;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.menu.type.AbstractClaimMenu;
import su.nightexpress.excellentclaims.menu.type.ClaimMenu;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class SettingsMenu extends AbstractClaimMenu implements ConfigBased, ClaimMenu {

    public SettingsMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, BLACK.wrap("Claim Settings: " + CLAIM_NAME), ClaimPermission.MANAGE_CLAIM);
    }

    @Override
    @Nullable
    protected AbstractClaimMenu getBackMenu() {
        return null;
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }

//    @Override
//    @NotNull
//    protected String getTitle(@NotNull MenuViewer viewer) {
//        return this.getClaim(viewer).replacePlaceholders().apply(this.title);
//    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    protected void onItemPrepare(@NotNull MenuViewer viewer, @NotNull MenuItem menuItem, @NotNull NightItem item) {
        super.onItemPrepare(viewer, menuItem, item);

        item.replacement(replacer -> replacer.replace(this.getLink(viewer).replacePlaceholders()));
    }

    private void handleRemove(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(viewer);

        this.runNextTick(() -> UIUtils.openConfirmation(player, Confirmation.builder()
            .onAccept((viewer1, event1) -> {
                if (claim instanceof RegionClaim regionClaim) {
                    plugin.getClaimManager().removeRegion(player, regionClaim);
                }
                else if (claim instanceof LandClaim landClaim) {
                    plugin.getClaimManager().unclaimChunk(player, landClaim);
                }
                this.runNextTick(player::closeInventory);
            })
            .onReturn((viewer1, event1) -> {
                this.runNextTick(() -> this.open(player, claim));
            })
            .returnOnAccept(false)
            .build()));
    }

    private void handleRename(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        this.handleInput(Dialog.builder(viewer, Lang.CLAIM_RENAME_PROMPT, input -> {
            plugin.getClaimManager().setName(viewer.getPlayer(), this.getLink(viewer), input.getText());
            return true;
        }));
    }

    private void handleDescription(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        this.handleInput(Dialog.builder(viewer, Lang.CLAIM_DESCRIPTION_PROMPT, input -> {
            plugin.getClaimManager().setDescription(viewer.getPlayer(), this.getLink(viewer), input.getText());
            return true;
        }));
    }

    private void handleIcon(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        //Player player = viewer.getPlayer();
        Claim claim = this.getLink(viewer);
        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType().isAir()) return;

        claim.setIcon(NightItem.fromItemStack(cursor));
        claim.setSaveRequired(true);

        this.runNextTick(() -> this.flush(viewer));
    }

    private void handleMembers(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        this.runNextTick(() -> plugin.getMenuManager().openMembersMenu(viewer.getPlayer(), this.getLink(viewer)));
    }

    private void handleFlags(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(viewer.getPlayer(), this.getLink(viewer)));
    }

    private void handlePriority(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        Claim claim = this.getLink(viewer);
        this.handleInput(Dialog.builder(viewer, Lang.CLAIM_PRIORITY_PROMPT, input -> {
            claim.setPriority(input.asIntAbs());
            claim.setSaveRequired(true);
            return true;
        }));
    }

    private void handleTeleport(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        this.runNextTick(() -> this.getLink(viewer).teleport(viewer.getPlayer()));
    }

    private void handleTransfer(@NotNull MenuViewer viewer, @NotNull InventoryClickEvent event) {
        this.runNextTick(() -> plugin.getMenuManager().openTransferMenu(viewer.getPlayer(), this.getLink(viewer)));
    }

    private void handleMerge(@NotNull MenuViewer viewer, @NotNull MergeType type) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(viewer);
        if (!(claim instanceof LandClaim landClaim)) return;

        this.plugin.getClaimManager().startMerge(player, type, landClaim);
        this.runNextTick(player::closeInventory);
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(IntStream.range(45, 54).toArray())
        );

        loader.addDefaultItem(NightItem.fromType(Material.GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(IntStream.range(20, 25).toArray())
        );


        loader.addDefaultItem(NightItem.fromType(Material.BARRIER)
            .setDisplayName(RED.wrap(BOLD.wrap("Remove Claim")))
            .setLore(Lists.newList(
                GRAY.wrap("Remove claim permanently."),
                "",
                RED.wrap("→ " + UNDERLINED.wrap("Click to remove"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(45)
            .setHandler(this.createHandler("claim_remove", this::handleRemove, ClaimPermission.REMOVE_CLAIM))
        );

        loader.addDefaultItem(NightItem.fromType(Material.GLOW_ITEM_FRAME)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Icon")))
            .setLore(Lists.newList(
                GRAY.wrap("Represents the claim in GUIs."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Drag'n'Drop to change"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(11)
            .setHandler(this.createHandler("claim_icon", this::handleIcon, ClaimPermission.SET_CLAIM_ICON))
        );

        loader.addDefaultItem(NightItem.fromType(Material.BIRCH_HANGING_SIGN)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Name")))
            .setLore(Lists.newList(
                GRAY.wrap("Current: " + LIGHT_YELLOW.wrap(CLAIM_NAME)),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to change"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(13)
            .setHandler(this.createHandler("claim_name", this::handleRename, ClaimPermission.RENAME_CLAIM))
        );

        loader.addDefaultItem(NightItem.fromType(Material.LECTERN)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Description")))
            .setLore(Lists.newList(
                GRAY.wrap("Current: " + LIGHT_YELLOW.wrap(CLAIM_DESCRIPTION)),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to change"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(15)
            .setHandler(this.createHandler("claim_description", this::handleDescription, ClaimPermission.SET_CLAIM_DESCRIPTION))
        );

        loader.addDefaultItem(NightItem.fromType(Material.KNOWLEDGE_BOOK)
            .setDisplayName(GREEN.wrap(BOLD.wrap("Members")))
            .setLore(Lists.newList(
                GRAY.wrap("View and manage claim members."),
                "",
                GREEN.wrap("→ " + UNDERLINED.wrap("Click to open"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(29)
            .setHandler(this.createHandler("claim_members", this::handleMembers, ClaimPermission.MANAGE_MEMBERS))
        );

        loader.addDefaultItem(NightItem.fromType(Material.RED_BANNER)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Flags")))
            .setLore(Lists.newList(
                GRAY.wrap("View and manage claim flags."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to open"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(30)
            .setHandler(this.createHandler("claim_flags", this::handleFlags, ClaimPermission.MANAGE_FLAGS))
        );

        loader.addDefaultItem(NightItem.fromType(Material.ENDER_PEARL)
            .setDisplayName(LIGHT_PURPLE.wrap(BOLD.wrap("Teleport")))
            .setLore(Lists.newList(
                GRAY.wrap("Teleport to claim's spawn point."),
                "",
                LIGHT_PURPLE.wrap("→ " + UNDERLINED.wrap("Click to teleport"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(31)
            .setHandler(this.createHandler("claim_teleport", this::handleTeleport, ClaimPermission.TELEPORT))
        );

        loader.addDefaultItem(NightItem.fromType(Material.COMPARATOR)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Priority")))
            .setLore(Lists.newList(
                GRAY.wrap("Current: " + LIGHT_YELLOW.wrap(CLAIM_PRIORITY)),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to change"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(32)
            .setHandler(this.createHandler("claim_priority", this::handlePriority, ClaimPermission.SET_CLAIM_PRIORITY))
        );

        loader.addDefaultItem(NightItem.fromType(Material.TRIAL_KEY)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Transfer Ownership")))
            .setLore(Lists.newList(
                GRAY.wrap("Transfer claim to other player."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to open"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(33)
            .setHandler(this.createHandler("claim_transfer", this::handleTransfer, ClaimPermission.TRANSFER_CLAIM))
        );



        // ------------ Chunks only ------------ //

        loader.addDefaultItem(NightItem.fromType(Material.WATER_BUCKET)
            .setDisplayName(LIGHT_BLUE.wrap(BOLD.wrap("Merge Claim")))
            .setLore(Lists.newList(
                GRAY.wrap("Merge chunks(s) of this claim"),
                GRAY.wrap("into another claim."),
                "",
                LIGHT_BLUE.wrap("→ " + UNDERLINED.wrap("Click to merge"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(51)
            .setHandler(this.createHandler("claim_merge", (viewer, event) -> this.handleMerge(viewer, MergeType.MERGE), ClaimPermission.MERGE_CLAIM, ClaimType.CHUNK))
        );

        loader.addDefaultItem(NightItem.fromType(Material.LAVA_BUCKET)
            .setDisplayName(LIGHT_ORANGE.wrap(BOLD.wrap("Split Claim")))
            .setLore(Lists.newList(
                GRAY.wrap("Separate a chunk of this"),
                GRAY.wrap("claim as a dedicated claim."),
                "",
                LIGHT_ORANGE.wrap("→ " + UNDERLINED.wrap("Click to separate"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setPriority(10)
            .setSlots(47)
            .setHandler(this.createHandler("claim_separate", (viewer, event) -> this.handleMerge(viewer, MergeType.SPLIT), ClaimPermission.SPLIT_CLAIM, ClaimType.CHUNK))
        );
    }

    @NotNull
    private ItemHandler createHandler(@NotNull String name, @NotNull ItemClick click, @NotNull ClaimPermission permission) {
        return this.createHandler(name, click, permission, null);
    }

    @NotNull
    private ItemHandler createHandler(@NotNull String name, @NotNull ItemClick click, @NotNull ClaimPermission permission, @Nullable ClaimType claimType) {
        ItemOptions options = ItemOptions.builder()
            .setVisibilityPolicy(viewer -> (claimType == null || this.getLink(viewer).getType() == claimType) && this.getLink(viewer).hasPermission(viewer.getPlayer(), permission))
            .build();

        return new ItemHandler(name, click, options);
    }
}
