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
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@SuppressWarnings("UnstableApiUsage")
public class MembersMenu extends LinkedMenu<ClaimPlugin, Claim> implements Filled<Member>, ConfigBased {

    public static final String FILE_NAME = "claim_members.yml";

    private static final String LORE_MANAGE = "%manage%";
    private static final String SKIN_PLUS = "5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1";

    private String       memberName;
    private List<String> memberLore;
    private int[]        memberSlots;

    private List<String> loreManage;

    public MembersMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, BLACK.wrap("Claim Members of " + CLAIM_NAME));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
    }

    @Override
    public @NotNull MenuFiller<Member> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);

        var autoFill = MenuFiller.builder(this);

        autoFill.setSlots(this.memberSlots);
        autoFill.setItems(claim.getMembers().stream().sorted(Comparator.comparingInt((Member mem) -> mem.getRank().getPriority()).reversed()).toList());
        autoFill.setItemCreator(member -> {
            return new NightItem(Material.PLAYER_HEAD)
                .setSkullOwner(member.getOfflinePlayer())
                .setDisplayName(this.memberName)
                .setLore(this.memberLore)
                .replacement(replacer -> replacer
                    .replace(GENERIC_NAME, member.getPlayerName())
                    .replace(member.getRank().replacePlaceholders())
                    .replace(LORE_MANAGE, this.canManage(player, claim) ? this.loreManage : Collections.emptyList()));
        });
        autoFill.setItemClick(member -> (viewer1, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openMemberMenu(player, claim, member));
        });

        return autoFill.build();
    }

    private boolean canManage(@NotNull Player player, @NotNull Claim claim) {
        return claim.hasPermission(player, ClaimPermission.MANAGE_MEMBERS);
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
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.memberName = ConfigValue.create("Member.Name",
            LIGHT_YELLOW.wrap(BOLD.wrap(GENERIC_NAME))
        ).read(config);

        this.memberLore = ConfigValue.create("Member.Lore", Lists.newList(
            LIGHT_GRAY.wrap("Rank: " + LIGHT_YELLOW.wrap(RANK_NAME)),
            EMPTY_IF_BELOW,
            RANK_PERMISSIONS,
            EMPTY_IF_BELOW,
            LORE_MANAGE
        )).read(config);

        this.memberSlots = ConfigValue.create("Member.Slots", IntStream.range(0, 36).toArray()).read(config);

        this.loreManage = ConfigValue.create("Member.Manage", Lists.newList(
            LIGHT_GRAY.wrap(LIGHT_YELLOW.wrap("[▶]") + " Click to " + LIGHT_YELLOW.wrap("manage") + ".")
        )).read(config);

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_PLUS)
            .setDisplayName(LIGHT_GREEN.wrap(BOLD.wrap("Add Member")))
            .toMenuItem()
            .setPriority(10).setSlots(41).setHandler(new ItemHandler("add_member", this.manageLink((viewer, event, claim) -> {
                this.handleInput(Dialog.builder(viewer, Lang.MEMBER_ADD_PROMPT, input -> {
                    this.plugin.getMemberManager().addMember(viewer.getPlayer(), claim, input.getTextRaw());
                    return true;
                }));
            }), ItemOptions.builder()
                .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.ADD_MEMBERS))
                .build())
            )
        );

        loader.addDefaultItem(MenuItem.buildNextPage(this, 44));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 36));
        loader.addDefaultItem(MenuItem.buildReturn(this, 39, this.manageLink((viewer, event, claim) -> {
            this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(viewer.getPlayer(), claim));
        }), ItemOptions.builder()
            .setVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_CLAIM))
            .build())
        );
    }
}
