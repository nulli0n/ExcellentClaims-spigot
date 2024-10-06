package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.*;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.click.ClickResult;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class ClaimMenu extends ConfigMenu<ClaimPlugin> implements Linked<Claim> {

    public static final String FILE_NAME = "claim_menu.yml";

    private final ViewLink<Claim> link;

    private final ItemHandler removeHandler;
    private final ItemHandler nameHandler;
    private final ItemHandler descriptionHandler;
    private final ItemHandler iconHandler;
    private final ItemHandler priorityHandler;
    private final ItemHandler teleportHandler;
    private final ItemHandler membersHandler;
    private final ItemHandler flagsHandler;
    private final ItemHandler transferHandler;

    private final ItemHandler mergeHandler;
    private final ItemHandler separateHandler;

    public ClaimMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.removeHandler = new ItemHandler("claim_remove", (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player);

            this.plugin.getMenuManager().openConfirm(player, Confirmation.create(
                (viewer1, event1) -> {
                    if (claim instanceof RegionClaim regionClaim) {
                        plugin.getClaimManager().removeRegion(player, regionClaim);
                    }
                    else if (claim instanceof ChunkClaim chunkClaim) {
                        plugin.getClaimManager().unclaimChunk(player, chunkClaim);
                    }
                    this.runNextTick(player::closeInventory);
                },
                (viewer1, event1) -> {
                    this.runNextTick(() -> this.open(player, claim));
                }
            ));
        }));

        this.addHandler(this.nameHandler = new ItemHandler("claim_name", (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player);
            Dialog.create(player, (dialog, input) -> {
                plugin.getClaimManager().setName(player, claim, input.getText());
                return true;
            });
            this.runNextTick(player::closeInventory);
            Lang.CLAIM_RENAME_PROMPT.getMessage().send(player);
        }));

        this.addHandler(this.descriptionHandler = new ItemHandler("claim_description", (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player);
            Dialog.create(player, (dialog, input) -> {
                plugin.getClaimManager().setDescription(player, claim, input.getText());
                return true;
            });
            this.runNextTick(player::closeInventory);
            Lang.CLAIM_DESCRIPTION_PROMPT.getMessage().send(player);
        }));

        this.addHandler(this.iconHandler = new ItemHandler("claim_icon", (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player);
            ItemStack cursor = event.getCursor();
            if (cursor == null || cursor.getType().isAir()) return;

            claim.setIcon(cursor);
            claim.setSaveRequired(true);

            this.runNextTick(() -> this.flush(viewer));
        }));

        this.addHandler(this.priorityHandler = new ItemHandler("claim_priority", (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player);
            Dialog.create(player, (dialog, input) -> {
                claim.setPriority(input.asInt());
                claim.setSaveRequired(true);
                return true;
            });
            this.runNextTick(player::closeInventory);
            Lang.CLAIM_PRIORITY_PROMPT.getMessage().send(player);
        }));

        this.addHandler(this.teleportHandler = new ItemHandler("claim_teleport", (viewer, event) -> {
            this.runNextTick(() -> this.getLink(viewer).teleport(viewer.getPlayer()));
        }));

        this.addHandler(this.membersHandler = new ItemHandler("claim_members", (viewer, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openMembersMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addHandler(this.flagsHandler = new ItemHandler("claim_flags", (viewer, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addHandler(this.transferHandler = new ItemHandler("claim_transfer", (viewer, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openTransferMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addHandler(this.mergeHandler = new ItemHandler("claim_merge", (viewer, event) -> {
            this.onClickMerge(viewer, MergeType.MERGE);
        }));

        this.addHandler(this.separateHandler = new ItemHandler("claim_separate", (viewer, event) -> {
            this.onClickMerge(viewer, MergeType.SEPARATE);
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            ItemHandler handler = menuItem.getHandler();

            menuItem.getOptions().addDisplayModifier((viewer, itemStack) -> {
                Player player = viewer.getPlayer();
                Claim claim = this.getLink(player);

                if (handler == this.iconHandler) {
                    ItemMeta originMeta = itemStack.getItemMeta();
                    ItemStack icon = claim.getIcon();
                    itemStack.setType(icon.getType());
                    itemStack.setAmount(icon.getAmount());
                    itemStack.setItemMeta(icon.getItemMeta());

                    if (originMeta != null) {
                        ItemUtil.editMeta(itemStack, meta -> {
                            meta.setDisplayName(originMeta.getDisplayName());
                            meta.setLore(originMeta.getLore());
                        });
                    }
                }

                ItemReplacer.replace(itemStack, this.getLink(viewer).getPlaceholders());
            });

            menuItem.getOptions().setVisibilityPolicy(viewer -> {
                Player player = viewer.getPlayer();
                Claim claim = this.getLink(player);

                if (handler == this.nameHandler) {
                    return claim.hasPermission(player, ClaimPermission.RENAME_CLAIM);
                }
                else if (handler == this.removeHandler) {
                    return claim.hasPermission(player, ClaimPermission.REMOVE_CLAIM);
                }
                else if (handler == this.descriptionHandler) {
                    return claim.hasPermission(player, ClaimPermission.SET_CLAIM_DESCRIPTION);
                }
                else if (handler == this.iconHandler) {
                    return claim.hasPermission(player, ClaimPermission.SET_CLAIM_ICON);
                }
                else if (handler == this.priorityHandler) {
                    return claim.hasPermission(player, ClaimPermission.SET_CLAIM_PRIORITY) && player.hasPermission(Perms.CLAIMS_PRIORITY);
                }
                else if (handler == this.teleportHandler) {
                    return claim.hasPermission(player, ClaimPermission.TELEPORT);
                }
                else if (handler == this.flagsHandler) {
                    return claim.hasPermission(player, ClaimPermission.MANAGE_FLAGS);
                }
                else if (handler == this.membersHandler) {
                    return claim.hasPermission(player, ClaimPermission.MANAGE_MEMBERS);
                }
                else if (handler == this.transferHandler) {
                    return claim.hasPermission(player, ClaimPermission.TRANSFER_CLAIM);
                }
                else if (handler == this.mergeHandler) {
                    return claim.getType() == ClaimType.CHUNK && claim.hasPermission(player, ClaimPermission.MERGE_CLAIM);
                }
                else if (handler == this.separateHandler) {
                    return claim.getType() == ClaimType.CHUNK && claim.hasPermission(player, ClaimPermission.SEPARATE_CLAIM);
                }
                return true;
            });
        });
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }

    @Override
    @NotNull
    public ViewLink<Claim> getLink() {
        return this.link;
    }

    private void onClickMerge(@NotNull MenuViewer viewer, @NotNull MergeType type) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);
        if (!(claim instanceof ChunkClaim chunkClaim)) return;

        this.plugin.getClaimManager().startMerge(player, type, chunkClaim);
        this.runNextTick(player::closeInventory);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        options.editTitle(title -> this.getLink(viewer).replacePlaceholders().apply(title));
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Claim Settings: " + CLAIM_NAME), MenuSize.CHEST_45);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();


        ItemStack removeItem = ItemUtil.getSkinHead("3e17932e01422531653739d3becfbb5cf78f5f76a3d7b769e2f661f52c2af2da");
        ItemUtil.editMeta(removeItem, meta -> {
            meta.setDisplayName(RED.enclose(BOLD.enclose("Remove Claim")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Remove claim permanently."),
                "",
                LIGHT_GRAY.enclose(RED.enclose("[▶]") + " Click to " + RED.enclose("remove") + ".")
            ));
        });
        list.add(new MenuItem(removeItem).setPriority(10).setSlots(8).setHandler(this.removeHandler));


        ItemStack nameItem = ItemUtil.getSkinHead("f5a19af0e61ca42532c0599fa0a391753df6b71f9fa4a177f1aa9b1d81fe6ee2");
        ItemUtil.editMeta(nameItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Display Name")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Current: " + LIGHT_YELLOW.enclose(CLAIM_NAME)),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("change") + ".")
            ));
        });
        list.add(new MenuItem(nameItem).setPriority(10).setSlots(11).setHandler(this.nameHandler));


        ItemStack descItem = ItemUtil.getSkinHead("1d111a029754d5d2681b65ade843b721d0a814a80dc16a38ea04cae61913ae20");
        ItemUtil.editMeta(descItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Description")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Current: " + LIGHT_YELLOW.enclose(CLAIM_DESCRIPTION)),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("change") + ".")
            ));
        });
        list.add(new MenuItem(descItem).setPriority(10).setSlots(12).setHandler(this.descriptionHandler));


        ItemStack iconItem = ItemUtil.getSkinHead("89f7a04ac334fcaf618da9e841f03c00d749002dc592f8540ef9534442cecf42");
        ItemUtil.editMeta(iconItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Icon")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Represents the claim in GUIs."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Drag-n-Drop to " + LIGHT_YELLOW.enclose("change") + ".")
            ));
        });
        list.add(new MenuItem(iconItem).setPriority(10).setSlots(13).setHandler(this.iconHandler));


        ItemStack membersItem = ItemUtil.getSkinHead("97e6e5657b8f85f3af2c835b3533856607682f8571a4548967e2bdb535ac56b7");
        ItemUtil.editMeta(membersItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Members")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("View and manage claim members."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")
            ));
        });
        list.add(new MenuItem(membersItem).setPriority(10).setSlots(14).setHandler(this.membersHandler));


        ItemStack flagsItem = ItemUtil.getSkinHead("88366f62f0e8ec73145937b3c9b3be646b175132c97d0738b2b2dc84ae424e61");
        ItemUtil.editMeta(flagsItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Flags")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("View and manage claim flags."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")
            ));
        });
        list.add(new MenuItem(flagsItem).setPriority(10).setSlots(15).setHandler(this.flagsHandler));


        ItemStack priorityItem = ItemUtil.getSkinHead("5daf7746ec23ffa4667cf64ba72c941e1683a9c7794875b8428139867642fdf");
        ItemUtil.editMeta(priorityItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Priority")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Current: " + LIGHT_YELLOW.enclose(CLAIM_PRIORITY)),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("change") + ".")
            ));
        });
        list.add(new MenuItem(priorityItem).setPriority(10).setSlots(21).setHandler(this.priorityHandler));


        ItemStack teleportItem = ItemUtil.getSkinHead("b0bfc2577f6e26c6c6f7365c2c4076bccee653124989382ce93bca4fc9e39b");
        ItemUtil.editMeta(teleportItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Teleport")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Teleport to claim's spawn point."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("teleport") + ".")
            ));
        });
        list.add(new MenuItem(teleportItem).setPriority(10).setSlots(22).setHandler(this.teleportHandler));

        ItemStack transferItem = ItemUtil.getSkinHead("29142c7688f08447dd0c1aa6b5768b3d6767c64ea93046fd38e5833fa6702ea1");
        ItemUtil.editMeta(transferItem, meta -> {
            meta.setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Transfer Ownership")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Transfer claim to other player."),
                "",
                LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")
            ));
        });
        list.add(new MenuItem(transferItem).setPriority(10).setSlots(23).setHandler(this.transferHandler));

        // ------------ Chunks only ------------ //

        ItemStack mergeItem = ItemUtil.getSkinHead("e7742034f59db890c8004156b727c77ca695c4399d8e0da5ce9227cf836bb8e2");
        ItemUtil.editMeta(mergeItem, meta -> {
            meta.setDisplayName(CYAN.enclose(BOLD.enclose("Merge Claim")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Merge chunks(s) of this claim"),
                LIGHT_GRAY.enclose("into another claim."),
                "",
                LIGHT_GRAY.enclose(CYAN.enclose("[▶]") + " Click to " + CYAN.enclose("merge") + ".")
            ));
        });
        list.add(new MenuItem(mergeItem).setPriority(10).setSlots(0).setHandler(this.mergeHandler));


        ItemStack separateItem = ItemUtil.getSkinHead("f3514f23d6b09e1840cdec7c0d6912dcd30f82110858c133a7f7778c728566dd");
        ItemUtil.editMeta(separateItem, meta -> {
            meta.setDisplayName(ORANGE.enclose(BOLD.enclose("Separate Claim")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Separate a chunk of this"),
                LIGHT_GRAY.enclose("claim as a dedicated claim."),
                "",
                LIGHT_GRAY.enclose(ORANGE.enclose("[▶]") + " Click to " + ORANGE.enclose("separate") + ".")
            ));
        });
        list.add(new MenuItem(separateItem).setPriority(10).setSlots(9).setHandler(this.separateHandler));


        return list;
    }

    @Override
    protected void loadAdditional() {

    }
}
