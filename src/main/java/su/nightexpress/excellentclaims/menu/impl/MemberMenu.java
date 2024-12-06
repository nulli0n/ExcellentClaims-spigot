package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.member.Member;
import su.nightexpress.excellentclaims.api.member.MemberRank;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
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
public class MemberMenu extends LinkedMenu<ClaimPlugin, MemberMenu.Data> implements ConfigBased {

    public static final String FILE_NAME = "claim_member.yml";

    private static final String NEXT_RANK     = "%next_rank%";
    private static final String PREVIOUS_RANK = "%previous_rank%";

    private static final String SKIN_UP   = "77334cddfab45d75ad28e1a47bf8cf5017d2f0982f6737da22d4972952510661";
    private static final String SKIN_DOWN = "7189c997db7cbfd632c2298f6db0c0a3dd4fc4cbbb278be75484fc82c6b806d4";
    private static final String SKIN_LOCK = "e6015b480ac2ce4834c9d8f2ca5d15c6cac38a52147040a1c4c095a2319816f5";

    private String noNextRank;
    private String noPrevRank;

    public record Data(@NotNull Claim claim, @NotNull Member member) {}

    public MemberMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X4, BLACK.enclose("Member Settings: " + PLAYER_NAME));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
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

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_LOCK)
            .setDisplayName(DARK_GRAY.enclose("Promote Member") + " " + RED.enclose("[Locked]"))
            .setLore(Lists.newList(
                LIGHT_GRAY.enclose("This action is not available.")
            ))
            .toMenuItem().setPriority(1).setSlots(11)
        );

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_LOCK)
            .setDisplayName(DARK_GRAY.enclose("Demote Member") + " " + RED.enclose("[Locked]"))
            .setLore(Lists.newList(
                LIGHT_GRAY.enclose("This action is not available.")
            ))
            .toMenuItem().setPriority(1).setSlots(13)
        );

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_LOCK)
            .setDisplayName(DARK_GRAY.enclose("Kick Member") + " " + RED.enclose("[Locked]"))
            .setLore(Lists.newList(
                LIGHT_GRAY.enclose("This action is not available.")
            ))
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

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_UP)
            .setDisplayName(CYAN.enclose(BOLD.enclose("Promote Member")))
            .setLore(Lists.newList(
                LIGHT_GRAY.enclose("Promotion: " + CYAN.enclose(RANK_NAME) + " → " + CYAN.enclose(NEXT_RANK)),
                "",
                LIGHT_GRAY.enclose(CYAN.enclose("[▶]") + " Click to " + CYAN.enclose("promote") + ".")
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

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_DOWN)
            .setDisplayName(ORANGE.enclose(BOLD.enclose("Demote Member")))
            .setLore(Lists.newList(
                LIGHT_GRAY.enclose("Demotion: " + ORANGE.enclose(RANK_NAME)) + " → " + ORANGE.enclose(PREVIOUS_RANK),
                "",
                LIGHT_GRAY.enclose(ORANGE.enclose("[▶]") + " Click to " + ORANGE.enclose("demote") + ".")
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

        loader.addDefaultItem(NightItem.asCustomHead("bb72ad8369eb6cd8990cec1f54d1778442a108b0186622c5918eb85159e2fb9e")
            .setDisplayName(RED.enclose(BOLD.enclose("Kick Member")))
            .setLore(Lists.newList(
                LIGHT_GRAY.enclose("Revoke all claim permissions and"),
                LIGHT_GRAY.enclose("remove it from the claim completely."),
                "",
                LIGHT_GRAY.enclose(RED.enclose("[▶]") + " Click to " + RED.enclose("kick") + ".")
            ))
            .toMenuItem()
            .setPriority(10).setSlots(15).setHandler(new ItemHandler("kick", this.manageLink((viewer, event, data) -> {
                Player player = viewer.getPlayer();
                if (!this.plugin.getMemberManager().canKick(player, data.claim, data.member)) return;

                this.plugin.getMenuManager().openConfirm(player, Confirmation.create(
                    (viewer1, event1) -> {
                        data.claim.removeMember(data.member);
                        data.claim.setSaveRequired(true);
                        this.returnToMembers(viewer1, data);
                    },
                    (viewer1, event1) -> {
                        this.runNextTick(() -> this.open(player, data));
                    }
                ));
            }), ItemOptions.builder()
                .setVisibilityPolicy(this::canKick)
                .setDisplayModifier(displayMod)
                .build())
            )
        );

        // Generic

        loader.addDefaultItem(NightItem.asCustomHead(SKIN_ARROW_DOWN)
            .localized(Lang.EDITOR_ITEM_RETURN)
            .toMenuItem()
            .setPriority(10)
            .setSlots(31)
            .setHandler(ItemHandler.forReturn(this, (viewer, event) -> {
                this.returnToMembers(viewer);
            }))
        );
    }
}
