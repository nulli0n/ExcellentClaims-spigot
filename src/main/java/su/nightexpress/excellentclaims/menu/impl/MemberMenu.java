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
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.menu.type.ClaimMenu;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.UIUtils;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.confirmation.Confirmation;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.PLAYER_NAME;
import static su.nightexpress.excellentclaims.Placeholders.RANK_NAME;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class MemberMenu extends LinkedMenu<ClaimPlugin, MemberMenu.Data> implements ConfigBased, ClaimMenu {

    public static final String FILE_NAME = "claim_member.yml";

    private static final String NEXT_RANK     = "%next_rank%";
    private static final String PREVIOUS_RANK = "%previous_rank%";

    private String noNextRank;
    private String noPrevRank;

    public record Data(@NotNull Claim claim, @NotNull Member member) {}

    public MemberMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X4, BLACK.wrap("Member Settings: " + PLAYER_NAME));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
    }

    @Override
    public boolean hasPermission(@NotNull Player player, @NotNull Claim claim) {
        return claim.hasPermission(player, ClaimPermission.MANAGE_MEMBERS);
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

    private boolean canPromote(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        return this.plugin.getMemberManager().canPromote(viewer.getPlayer(), data.claim, data.member);
    }

    private boolean canDemote(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        return this.plugin.getMemberManager().canDemote(viewer.getPlayer(), data.claim, data.member);
    }

    private boolean canKick(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        return this.plugin.getMemberManager().canKick(viewer.getPlayer(), data.claim, data.member);
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);

        return this.title.replace(PLAYER_NAME, data.member.getPlayerName());
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.noNextRank = ConfigValue.create("Lore.NoNextRank", "<Max. Rank>").read(config);
        this.noPrevRank = ConfigValue.create("Lore.NoPreviousRank", "<Lowest Rank>").read(config);

        // Plugs

        loader.addDefaultItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setDisplayName(DARK_GRAY.wrap("Promote Member") + " " + RED.wrap("[Locked]"))
            .setLore(Lists.newList(GRAY.wrap("This action is not available.")))
            .toMenuItem().setPriority(1).setSlots(11)
        );

        loader.addDefaultItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setDisplayName(DARK_GRAY.wrap("Demote Member") + " " + RED.wrap("[Locked]"))
            .setLore(Lists.newList(GRAY.wrap("This action is not available.")))
            .toMenuItem().setPriority(1).setSlots(13)
        );

        loader.addDefaultItem(NightItem.fromType(Material.GRAY_STAINED_GLASS_PANE)
            .setDisplayName(DARK_GRAY.wrap("Kick Member") + " " + RED.wrap("[Locked]"))
            .setLore(Lists.newList(GRAY.wrap("This action is not available.")))
            .toMenuItem().setPriority(1).setSlots(15)
        );

        // Real Items

        BiConsumer<MenuViewer, NightItem> displayMod = (viewer, item) -> {
            Player player = viewer.getPlayer();
            Data data = this.getLink(player);

            MemberRank nextRank = this.plugin.getMemberManager().getPromoteRank(player, data.claim, data.member);
            MemberRank prevRank = this.plugin.getMemberManager().getDemoteRank(player, data.claim, data.member);

            item.replacement(replacer -> replacer
                .replace(data.member.getRank().replacePlaceholders())
                .replace(NEXT_RANK, nextRank == null ? this.noNextRank : nextRank.getDisplayName())
                .replace(PREVIOUS_RANK, prevRank == null ? this.noPrevRank : prevRank.getDisplayName())
            );
        };

        loader.addDefaultItem(NightItem.fromType(Material.LIME_STAINED_GLASS_PANE)
            .setDisplayName(GREEN.wrap(BOLD.wrap("Promote Member")))
            .setLore(Lists.newList(
                GRAY.wrap("Promotion: " + GREEN.wrap(RANK_NAME) + " → " + GREEN.wrap(NEXT_RANK)),
                "",
                GREEN.wrap("→ " + UNDERLINED.wrap("Click to promote"))
            ))
            .toMenuItem()
            .setPriority(10).setSlots(11).setHandler(ItemHandler.forLink("promote", this, (viewer, event, data) -> {
                    MemberRank rank = this.plugin.getMemberManager().getPromoteRank(viewer.getPlayer(), data.claim, data.member);
                    if (rank == null) return;

                    data.member.setRank(rank);
                    data.claim.setSaveRequired(true);

                    this.runNextTick(() -> this.flush(viewer));
                }, ItemOptions.builder()
                    .setVisibilityPolicy(this::canPromote)
                    .setDisplayModifier(displayMod)
                    .build())
            )
        );

        loader.addDefaultItem(NightItem.fromType(Material.ORANGE_STAINED_GLASS_PANE)
            .setDisplayName(ORANGE.wrap(BOLD.wrap("Demote Member")))
            .setLore(Lists.newList(
                GRAY.wrap("Demotion: " + ORANGE.wrap(RANK_NAME) + " → " + ORANGE.wrap(PREVIOUS_RANK)),
                "",
                ORANGE.wrap("→ " + UNDERLINED.wrap("Click to demote"))
            ))
            .toMenuItem()
            .setPriority(10).setSlots(13).setHandler(new ItemHandler("demote", this.manageLink((viewer, event, data) -> {
                MemberRank rank = this.plugin.getMemberManager().getDemoteRank(viewer.getPlayer(), data.claim, data.member);
                if (rank == null) return;

                data.member.setRank(rank);
                data.claim.setSaveRequired(true);

                this.runNextTick(() -> this.flush(viewer));
            }), ItemOptions.builder()
                .setVisibilityPolicy(this::canDemote)
                .setDisplayModifier(displayMod)
                .build())
            )
        );

        loader.addDefaultItem(NightItem.fromType(Material.RED_STAINED_GLASS_PANE)
            .setDisplayName(RED.wrap(BOLD.wrap("Kick Member")))
            .setLore(Lists.newList(
                GRAY.wrap("Revoke all claim permissions and"),
                GRAY.wrap("remove it from the claim completely."),
                "",
                RED.wrap("→ " + UNDERLINED.wrap("Click to kick"))
            ))
            .toMenuItem()
            .setPriority(10).setSlots(15).setHandler(new ItemHandler("kick", this.manageLink((viewer, event, data) -> {
                Player player = viewer.getPlayer();
                if (!this.plugin.getMemberManager().canKick(player, data.claim, data.member)) return;

                this.runNextTick(() -> UIUtils.openConfirmation(player, Confirmation.builder()
                    .onAccept((viewer1, event1) -> {
                        data.claim.removeMember(data.member);
                        data.claim.setSaveRequired(true);
                        this.returnToMembers(viewer1, data);
                    })
                    .onReturn((viewer1, event1) -> {
                        this.runNextTick(() -> this.open(player, data));
                    })
                    .returnOnAccept(false)
                    .build()));

            }), ItemOptions.builder()
                .setVisibilityPolicy(this::canKick)
                .setDisplayModifier(displayMod)
                .build())
            )
        );

        loader.addDefaultItem(MenuItem.buildReturn(this, 31, (viewer, event) -> this.returnToMembers(viewer)));
        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setPriority(-1).setSlots(IntStream.range(27, 36).toArray()));
    }
}
