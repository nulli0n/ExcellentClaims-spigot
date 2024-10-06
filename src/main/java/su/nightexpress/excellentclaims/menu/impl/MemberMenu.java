package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
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
import java.util.function.BiConsumer;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;
import static su.nightexpress.excellentclaims.Placeholders.*;

public class MemberMenu extends ConfigMenu<ClaimPlugin> implements Linked<MemberMenu.Data> {

    public static final String FILE_NAME = "claim_member.yml";

    private static final String NEXT_RANK     = "%next_rank%";
    private static final String PREVIOUS_RANK = "%previous_rank%";

    private static final String SKIN_UP   = "77334cddfab45d75ad28e1a47bf8cf5017d2f0982f6737da22d4972952510661";
    private static final String SKIN_DOWN = "7189c997db7cbfd632c2298f6db0c0a3dd4fc4cbbb278be75484fc82c6b806d4";
    private static final String SKIN_LOCK = "e6015b480ac2ce4834c9d8f2ca5d15c6cac38a52147040a1c4c095a2319816f5";

    private final ItemHandler returnHandler;
    private final ItemHandler promoteHandler;
    private final ItemHandler demoteHandler;
    private final ItemHandler kickHandler;
    private final ViewLink<Data> link;

    private String noNextRank;
    private String noPrevRank;

    public MemberMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            this.returnToMembers(viewer);
        }));

        this.addHandler(this.promoteHandler = new ItemHandler("promote", (viewer, event) -> {
            this.onClick(viewer, (claim, member) -> {
                MemberRank rank = this.getPromoteRank(viewer.getPlayer(), claim, member);
                if (rank == null) return;

                member.setRank(rank);
                claim.setSaveRequired(true);

                this.runNextTick(() -> this.flush(viewer));
            });
        }));

        this.addHandler(this.demoteHandler = new ItemHandler("demote", (viewer, event) -> {
            this.onClick(viewer, (claim, member) -> {
                MemberRank rank = this.getDemoteRank(viewer.getPlayer(), claim, member);
                if (rank == null) return;

                member.setRank(rank);
                claim.setSaveRequired(true);

                this.runNextTick(() -> this.flush(viewer));
            });
        }));

        this.addHandler(this.kickHandler = new ItemHandler("kick", (viewer, event) -> {
            this.onClick(viewer, (claim, member) -> {
                Player player = viewer.getPlayer();
                if (!this.canKick(player, claim, member)) return;

                Data data = this.getLink(viewer);
                this.plugin.getMenuManager().openConfirm(player, Confirmation.create(
                    (viewer1, event1) -> {
                        claim.removeMember(member);
                        claim.setSaveRequired(true);
                        this.returnToMembers(viewer1, data);
                    },
                    (viewer1, event1) -> {
                        this.runNextTick(() -> this.open(player, data));
                        //this.returnToMembers(viewer1, data);
                    }
                ));
            });
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            ItemHandler handler = menuItem.getHandler();
            if (handler == this.promoteHandler) {
                menuItem.getOptions().addVisibilityPolicy(this::canPromote);
            }
            else if (handler == this.demoteHandler) {
                menuItem.getOptions().addVisibilityPolicy(this::canDemote);
            }
            else if (handler == this.kickHandler) {
                menuItem.getOptions().addVisibilityPolicy(this::canKick);
            }

            menuItem.getOptions().addDisplayModifier((viewer, itemStack) -> {
                Player player = viewer.getPlayer();
                Data data = this.getLink(player);

                ItemReplacer replacer = ItemReplacer.create(itemStack).readMeta().replace(data.member.getRank().getPlaceholders());

                if (handler == this.promoteHandler) {
                    MemberRank rank = this.getPromoteRank(player, data.claim, data.member);
                    replacer.replace(NEXT_RANK, rank == null ? this.noNextRank : rank.getDisplayName());
                }
                else if (handler == this.demoteHandler) {
                    MemberRank rank = this.getDemoteRank(player, data.claim, data.member);
                    replacer.replace(PREVIOUS_RANK, rank == null ? this.noPrevRank : rank.getDisplayName());
                }

                replacer.writeMeta();
            });
        });
    }

    @Override
    @NotNull
    public ViewLink<Data> getLink() {
        return this.link;
    }

    public void open(@NotNull Player player, @NotNull Claim claim, @NotNull Member member) {
        this.open(player, new Data(claim, member));
    }

    private void returnToMembers(@NotNull MenuViewer viewer) {
        this.returnToMembers(viewer, this.getLink(viewer));
    }

    private void returnToMembers(@NotNull MenuViewer viewer, @NotNull Data data) {
        this.runNextTick(() -> plugin.getMenuManager().openMembersMenu(viewer.getPlayer(), data.claim));
    }

    public boolean canPromote(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        return this.canPromote(viewer.getPlayer(), data.claim, data.member);
    }

    public boolean canDemote(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        return this.canDemote(viewer.getPlayer(), data.claim, data.member);
    }

    public boolean canKick(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        return this.canKick(viewer.getPlayer(), data.claim, data.member);
    }

    public boolean canPromote(@NotNull Player promoter, @NotNull Claim claim, @NotNull Member member) {
        return claim.hasPermission(promoter, ClaimPermission.PROMOTE_MEMBERS) && this.getPromoteRank(promoter, claim, member) != null;
    }

    public boolean canDemote(@NotNull Player promoter, @NotNull Claim claim, @NotNull Member member) {
        return claim.hasPermission(promoter, ClaimPermission.DEMOTE_MEMBERS) && this.getDemoteRank(promoter, claim, member) != null;
    }

    public boolean canKick(@NotNull Player kicker, @NotNull Claim claim, @NotNull Member member) {
        if (!plugin.getMemberManager().isAdminMode(kicker) && member.isPlayer(kicker)) return false;

        return claim.hasPermission(kicker, ClaimPermission.REMOVE_MEMBERS);
    }

    @Nullable
    public MemberRank getPromoteRank(@NotNull Player promoter, @NotNull Claim claim, @NotNull Member member) {
         boolean adminMode = plugin.getMemberManager().isAdminMode(promoter);

        if (!adminMode && member.isPlayer(promoter)) return null;
        if (claim.isOwner(member.getPlayerId())) return null;

        MemberRank rank = member.getRank();
        MemberRank nextRank = plugin.getMemberManager().getNextRank(rank);
        if (nextRank == null) return null;

        MemberRank userRank = claim.getMemberRank(promoter);
        if (!adminMode) {
            if (userRank == null || nextRank == userRank || nextRank.isAbove(userRank)) return null;
        }

        return nextRank;
    }

    @Nullable
    public MemberRank getDemoteRank(@NotNull Player demoter, @NotNull Claim claim, @NotNull Member member) {
        boolean adminMode = plugin.getMemberManager().isAdminMode(demoter);

        if (!adminMode && member.isPlayer(demoter)) return null;
        if (claim.isOwner(member.getPlayerId())) return null;

        MemberRank rank = member.getRank();
        MemberRank userRank = claim.getMemberRank(demoter);
        if (!adminMode) {
            if (userRank == null || rank == userRank || rank.isAbove(userRank)) return null;
        }

        return plugin.getMemberManager().getPreviousRank(rank);
    }

    private void onClick(@NotNull MenuViewer viewer, @NotNull BiConsumer<Claim, Member> consumer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        Claim claim = data.claim;
        Member member = data.member;

        consumer.accept(claim, member);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        Claim claim = data.claim;
        Member member = data.member;

        options.editTitle(str -> str.replace(PLAYER_NAME, member.getPlayerName()));
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Member Settings: " + PLAYER_NAME), MenuSize.CHEST_36);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        // Placeholders

        ItemStack promotePlug = ItemUtil.getSkinHead(SKIN_LOCK);
        ItemUtil.editMeta(promotePlug, meta -> {
            meta.setDisplayName(DARK_GRAY.enclose("Promote Member") + " " + RED.enclose("[Locked]"));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("This action is not available.")
            ));
        });
        list.add(new MenuItem(promotePlug).setPriority(1).setSlots(11));

        ItemStack demotePlug = ItemUtil.getSkinHead(SKIN_LOCK);
        ItemUtil.editMeta(demotePlug, meta -> {
            meta.setDisplayName(DARK_GRAY.enclose("Demote Member") + " " + RED.enclose("[Locked]"));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("This action is not available.")
            ));
        });
        list.add(new MenuItem(demotePlug).setPriority(1).setSlots(13));

        ItemStack kickPlug = ItemUtil.getSkinHead(SKIN_LOCK);
        ItemUtil.editMeta(kickPlug, meta -> {
            meta.setDisplayName(DARK_GRAY.enclose("Kick Member") + " " + RED.enclose("[Locked]"));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("This action is not available.")
            ));
        });
        list.add(new MenuItem(kickPlug).setPriority(1).setSlots(15));

        // Real Items

        ItemStack promoteItem = ItemUtil.getSkinHead(SKIN_UP);
        ItemUtil.editMeta(promoteItem, meta -> {
            meta.setDisplayName(CYAN.enclose(BOLD.enclose("Promote Member")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Promotion: " + CYAN.enclose(RANK_NAME) + " → " + CYAN.enclose(NEXT_RANK)),
                "",
                LIGHT_GRAY.enclose(CYAN.enclose("[▶]") + " Click to " + CYAN.enclose("promote") + ".")
            ));
        });
        list.add(new MenuItem(promoteItem).setPriority(10).setSlots(11).setHandler(this.promoteHandler));

        ItemStack demoteItem = ItemUtil.getSkinHead(SKIN_DOWN);
        ItemUtil.editMeta(demoteItem, meta -> {
            meta.setDisplayName(ORANGE.enclose(BOLD.enclose("Demote Member")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Demotion: " + ORANGE.enclose(RANK_NAME)) + " → " + ORANGE.enclose(PREVIOUS_RANK),
                "",
                LIGHT_GRAY.enclose(ORANGE.enclose("[▶]") + " Click to " + ORANGE.enclose("demote") + ".")
            ));
        });
        list.add(new MenuItem(demoteItem).setPriority(10).setSlots(13).setHandler(this.demoteHandler));

        ItemStack kickItem = ItemUtil.getSkinHead("bb72ad8369eb6cd8990cec1f54d1778442a108b0186622c5918eb85159e2fb9e");
        ItemUtil.editMeta(kickItem, meta -> {
            meta.setDisplayName(RED.enclose(BOLD.enclose("Kick Member")));
            meta.setLore(Lists.newList(
                LIGHT_GRAY.enclose("Revoke all claim permissions and"),
                LIGHT_GRAY.enclose("remove it from the claim completely."),
                "",
                LIGHT_GRAY.enclose(RED.enclose("[▶]") + " Click to " + RED.enclose("kick") + ".")
            ));
        });
        list.add(new MenuItem(kickItem).setPriority(10).setSlots(15).setHandler(this.kickHandler));

        // Generic

        ItemStack returnItem = ItemUtil.getSkinHead(SKIN_ARROW_DOWN);
        ItemUtil.editMeta(returnItem, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_RETURN.getLocalizedName());
        });
        list.add(new MenuItem(returnItem).setPriority(10).setSlots(31).setHandler(this.returnHandler));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.noNextRank = ConfigValue.create("Lore.NoNextRank", "<Max. Rank>").read(cfg);
        this.noPrevRank = ConfigValue.create("Lore.NoPreviousRank", "<Lowest Rank>").read(cfg);
    }

    public record Data(@NotNull Claim claim, @NotNull Member member) {}
}
