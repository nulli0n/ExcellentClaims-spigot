package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.*;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.menu.MenuClicks;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.click.ClickResult;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.function.BiConsumer;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@SuppressWarnings("UnstableApiUsage")
public class ClaimMenu extends LinkedMenu<ClaimPlugin, Claim> implements ConfigBased {

    public static final String FILE_NAME = "claim_menu.yml";

    public ClaimMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, BLACK.wrap("Claim Settings: " + CLAIM_NAME));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
    }

    @Override
    public void onClick(@NotNull MenuViewer viewer, @NotNull ClickResult result, @NotNull InventoryClickEvent event) {
        super.onClick(viewer, result, event);
        if (result.isInventory() && !event.isShiftClick()) {
            event.setCancelled(false);
        }
    }

    private void onClickMerge(@NotNull MenuViewer viewer, @NotNull MergeType type) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);
        if (!(claim instanceof LandClaim landClaim)) return;

        this.plugin.getClaimManager().startMerge(player, type, landClaim);
        this.runNextTick(player::closeInventory);
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        return this.getLink(viewer).replacePlaceholders().apply(this.title);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        BiConsumer<MenuViewer, NightItem> displayReplacer = (viewer, item) -> {
            item.replacement(replacer -> replacer.replace(this.getLink(viewer).replacePlaceholders()));
        };

        loader.addDefaultItem(NightItem.asCustomHead("3e17932e01422531653739d3becfbb5cf78f5f76a3d7b769e2f661f52c2af2da")
            .setDisplayName(RED.wrap(BOLD.wrap("Remove Claim")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Remove claim permanently."),
                "",
                LIGHT_GRAY.wrap(RED.wrap("[▶]") + " Click to " + RED.wrap("remove") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(8)
            .setHandler(new ItemHandler("claim_remove", MenuClicks.forClaimRemove(plugin, this), ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.REMOVE_CLAIM))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("f5a19af0e61ca42532c0599fa0a391753df6b71f9fa4a177f1aa9b1d81fe6ee2")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Display Name")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Current: " + LIGHT_YELLOW.wrap(CLAIM_NAME)),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("change") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(11)
            .setHandler(new ItemHandler("claim_name", this.manageLink((viewer, event, claim) -> {
                this.handleInput(Dialog.builder(viewer, Lang.CLAIM_RENAME_PROMPT, input -> {
                    plugin.getClaimManager().setName(viewer.getPlayer(), claim, input.getText());
                    return true;
                }));
            }), ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.RENAME_CLAIM))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("1d111a029754d5d2681b65ade843b721d0a814a80dc16a38ea04cae61913ae20")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Description")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Current: " + LIGHT_YELLOW.wrap(CLAIM_DESCRIPTION)),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("change") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(12)
            .setHandler(new ItemHandler("claim_description", this.manageLink((viewer, event, claim) -> {
                this.handleInput(Dialog.builder(viewer, Lang.CLAIM_DESCRIPTION_PROMPT, input -> {
                    plugin.getClaimManager().setDescription(viewer.getPlayer(), claim, input.getText());
                    return true;
                }));
            }), ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.SET_CLAIM_DESCRIPTION))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("89f7a04ac334fcaf618da9e841f03c00d749002dc592f8540ef9534442cecf42")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Icon")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Represents the claim in GUIs."),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Drag-n-Drop to " + LIGHT_YELLOW.wrap("change") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(13)
            .setHandler(new ItemHandler("claim_icon", (viewer, event) -> {
                Player player = viewer.getPlayer();
                Claim claim = this.getLink(player);
                ItemStack cursor = event.getCursor();
                if (cursor == null || cursor.getType().isAir()) return;

                claim.setIcon(NightItem.fromItemStack(cursor));
                claim.setSaveRequired(true);

                this.runNextTick(() -> this.flush(viewer));
            }, ItemOptions.builder()
                .setDisplayModifier((viewer, item) -> item.inherit(this.getLink(viewer).getIcon().copy().setDisplayName(item.getDisplayName()).setLore(item.getLore())))
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.SET_CLAIM_ICON))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("97e6e5657b8f85f3af2c835b3533856607682f8571a4548967e2bdb535ac56b7")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Members")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("View and manage claim members."),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("navigate") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(14)
            .setHandler(new ItemHandler("claim_members", (viewer, event) -> {
                this.runNextTick(() -> plugin.getMenuManager().openMembersMenu(viewer.getPlayer(), this.getLink(viewer)));
            }, ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_MEMBERS))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("88366f62f0e8ec73145937b3c9b3be646b175132c97d0738b2b2dc84ae424e61")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Flags")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("View and manage claim flags."),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("navigate") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(15)
            .setHandler(new ItemHandler("claim_flags", (viewer, event) -> {
                this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(viewer.getPlayer(), this.getLink(viewer)));
            }, ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_FLAGS))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("5daf7746ec23ffa4667cf64ba72c941e1683a9c7794875b8428139867642fdf")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Priority")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Current: " + LIGHT_YELLOW.wrap(CLAIM_PRIORITY)),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("change") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(21)
            .setHandler(new ItemHandler("claim_priority", this.manageLink((viewer, event, claim) -> {
                this.handleInput(Dialog.builder(viewer, Lang.CLAIM_PRIORITY_PROMPT, input -> {
                    claim.setPriority(input.asIntAbs());
                    claim.setSaveRequired(true);
                    return true;
                }));
            }), ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.SET_CLAIM_PRIORITY))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("b0bfc2577f6e26c6c6f7365c2c4076bccee653124989382ce93bca4fc9e39b")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Teleport")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Teleport to claim's spawn point."),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("teleport") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(22)
            .setHandler(new ItemHandler("claim_teleport", (viewer, event) -> {
                this.runNextTick(() -> this.getLink(viewer).teleport(viewer.getPlayer()));
            }, ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.TELEPORT))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("29142c7688f08447dd0c1aa6b5768b3d6767c64ea93046fd38e5833fa6702ea1")
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Transfer Ownership")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Transfer claim to other player."),
                "",
                LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("navigate") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(23)
            .setHandler(new ItemHandler("claim_transfer", (viewer, event) -> {
                this.runNextTick(() -> plugin.getMenuManager().openTransferMenu(viewer.getPlayer(), this.getLink(viewer)));
            }, ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.TRANSFER_CLAIM))
                .build())
            )
        );

        // ------------ Chunks only ------------ //

        loader.addDefaultItem(NightItem.asCustomHead("e7742034f59db890c8004156b727c77ca695c4399d8e0da5ce9227cf836bb8e2")
            .setDisplayName(CYAN.wrap(BOLD.wrap("Merge Claim")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Merge chunks(s) of this claim"),
                LIGHT_GRAY.wrap("into another claim."),
                "",
                LIGHT_GRAY.wrap(CYAN.wrap("[▶]") + " Click to " + CYAN.wrap("merge") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(0)
            .setHandler(new ItemHandler("claim_merge", (viewer, event) -> {
                this.onClickMerge(viewer, MergeType.MERGE);
            }, ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).getType() == ClaimType.CHUNK && this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.MERGE_CLAIM))
                .build())
            )
        );

        loader.addDefaultItem(NightItem.asCustomHead("f3514f23d6b09e1840cdec7c0d6912dcd30f82110858c133a7f7778c728566dd")
            .setDisplayName(ORANGE.wrap(BOLD.wrap("Separate Claim")))
            .setLore(Lists.newList(
                LIGHT_GRAY.wrap("Separate a chunk of this"),
                LIGHT_GRAY.wrap("claim as a dedicated claim."),
                "",
                LIGHT_GRAY.wrap(ORANGE.wrap("[▶]") + " Click to " + ORANGE.wrap("separate") + ".")
            ))
            .toMenuItem()
            .setPriority(10)
            .setSlots(9)
            .setHandler(new ItemHandler("claim_separate", (viewer, event) -> {
                this.onClickMerge(viewer, MergeType.SEPARATE);
            }, ItemOptions.builder()
                .setDisplayModifier(displayReplacer)
                .setVisibilityPolicy(viewer -> this.getLink(viewer).getType() == ClaimType.CHUNK && this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.SEPARATE_CLAIM))
                .build())
            )
        );
    }
}
