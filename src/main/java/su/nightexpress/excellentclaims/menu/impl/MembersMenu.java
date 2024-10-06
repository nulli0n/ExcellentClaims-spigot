package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.dialog.Dialog;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class MembersMenu extends ConfigMenu<ClaimPlugin> implements AutoFilled<Member>, Linked<Claim> {

    public static final String FILE_NAME = "claim_members.yml";

    private static final String LORE_MANAGE = "%manage%";
    private static final String SKIN_PLUS = "5ff31431d64587ff6ef98c0675810681f8c13bf96f51d9cb07ed7852b2ffd1";

    private final ItemHandler     returnHandler;
    private final ItemHandler     addHandler;
    private final ViewLink<Claim> link;

    private String       memberName;
    private List<String> memberLore;
    private int[]        memberSlots;

    private List<String> loreManage;

    public MembersMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(viewer.getPlayer(), this.getLink(viewer)));
        }));

        this.addHandler(this.addHandler = new ItemHandler("add_member", (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player);
            Dialog.create(player, (dialog, input) -> {
                this.plugin.getMemberManager().addMember(player, claim, input.getTextRaw());
                return true;
            });
            Lang.MEMBER_ADD_PROMPT.getMessage().send(player);
            this.runNextTick(player::closeInventory);
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            if (menuItem.getHandler() == this.addHandler) {
                menuItem.getOptions().addVisibilityPolicy(viewer -> this.getLink(viewer).hasPermission(viewer.getPlayer(), ClaimPermission.ADD_MEMBERS));
            }
            else if (menuItem.getHandler() == this.returnHandler) {
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
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<Member> autoFill) {
        Player player = viewer.getPlayer();
        Claim claim = this.getLink(player);

        autoFill.setSlots(this.memberSlots);
        autoFill.setItems(claim.getMembers().stream().sorted(Comparator.comparingInt((Member mem) -> mem.getRank().getPriority()).reversed()).toList());
        autoFill.setItemCreator(member -> {
            ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
            ItemUtil.editMeta(itemStack, meta -> {
                if (meta instanceof SkullMeta skullMeta) {
                    skullMeta.setOwningPlayer(member.getOfflinePlayer());
                }
            });

            ItemReplacer.create(itemStack).hideFlags().trimmed()
                .setDisplayName(this.memberName)
                .setLore(this.memberLore)
                .replace(GENERIC_NAME, member.getPlayerName())
                .replace(member.getRank().getPlaceholders())
                .replace(LORE_MANAGE, this.canManage(player, claim) ? this.loreManage : Collections.emptyList())
                .writeMeta();

            return itemStack;
        });
        autoFill.setClickAction(member -> (viewer1, event) -> {
            this.runNextTick(() -> plugin.getMenuManager().openMemberMenu(player, claim, member));
        });
    }

    private boolean canManage(@NotNull Player player, @NotNull Claim claim) {
        return claim.hasPermission(player, ClaimPermission.MANAGE_MEMBERS);
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
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Claim Members of " + CLAIM_NAME), MenuSize.CHEST_45);
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
        list.add(new MenuItem(returnItem).setPriority(10).setSlots(39).setHandler(this.returnHandler));

        ItemStack addItem = ItemUtil.getSkinHead(SKIN_PLUS);
        ItemUtil.editMeta(addItem, meta -> {
            meta.setDisplayName(LIGHT_GREEN.enclose(BOLD.enclose("Add Member")));
        });
        list.add(new MenuItem(addItem).setPriority(10).setSlots(41).setHandler(this.addHandler));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.memberName = ConfigValue.create("Member.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(GENERIC_NAME))
        ).read(cfg);

        this.memberLore = ConfigValue.create("Member.Lore", Lists.newList(
            LIGHT_GRAY.enclose("Rank: " + LIGHT_YELLOW.enclose(RANK_NAME)),
            "",
            RANK_PERMISSIONS,
            "",
            LORE_MANAGE
        )).read(cfg);

        this.memberSlots = ConfigValue.create("Member.Slots", IntStream.range(0, 36).toArray()).read(cfg);

        this.loreManage = ConfigValue.create("Member.Manage", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("manage") + ".")
        )).read(cfg);
    }
}
