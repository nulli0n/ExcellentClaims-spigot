package su.nightexpress.excellentclaims.region.ui.menu;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.region.RegionsPlaceholders;
import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.ui.RegionUIController;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RegionListMenu extends AbstractMenu {

    private static final String DEFAULT_TITLE = "My Regions";

    private final RegionsRepository  repository;
    private final RegionUIController controller;

    @Nullable
    private final RanksAPI ranksAPI;

    @Nullable
    private ItemPopulator<RegionClaim> regionItemPopulator;

    public RegionListMenu(ClaimPlugin plugin,
                          RegionsRepository repository,
                          RegionUIController controller,
                          @Nullable RanksAPI ranksAPI) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE);
        this.repository = repository;
        this.controller = controller;
        this.ranksAPI = ranksAPI;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 45).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());

        this.addNextPageButton(50);
        this.addPreviousPageButton(48);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {
        String regionName = config.getOrSet("Region.Name", ConfigCodecs.STRING, RegionsPlaceholders.REGION_NAME);

        List<String> regionLoreOwner = config.getOrSet("Region.Lore.Owner", ConfigCodecs.STRING_LIST, Lists.newList(
            TagWrappers.GREEN.wrap("✔ You are owner of this claim."),
            CommonPlaceholders.EMPTY_IF_BELOW,
            RegionsPlaceholders.REGION_DESCRIPTION,
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.GREEN.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to manage"))
        ));

        List<String> regionLoreMember = config.getOrSet("Region.Lore.Member", ConfigCodecs.STRING_LIST, Lists.newList(
            TagWrappers.YELLOW.wrap("✔ You are member of this claim."),
            CommonPlaceholders.EMPTY_IF_BELOW,
            RegionsPlaceholders.REGION_DESCRIPTION,
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.SPRITE_GUI.apply("mob_effect/haste") + " " +
                TagWrappers.GOLD.and(TagWrappers.BOLD).wrap("Your Permissions:"),
            ClaimsPlaceholders.RANK_PERMISSIONS,
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.YELLOW.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click for settings"))
        ));

        int[] regionSlots = config.getOrSet("Region.Slots", ConfigCodecs.INT_ARRAY, IntStream.range(0, 45).toArray());

        this.regionItemPopulator = ItemPopulator.builder(RegionClaim.class)
            .slots(regionSlots)
            .itemProvider((context, claim) -> {
                Player player = context.getPlayer();
                Rank rank = this.ranksAPI == null ? null : this.ranksAPI.resolveRank(claim, player.getUniqueId());

                return claim.getIcon()
                    .setDisplayName(regionName)
                    .setLore(claim.isOwner(player) ? regionLoreOwner : regionLoreMember)
                    .hideAllComponents()
                    .replace(ctx -> {
                        ctx.with(claim.placeholders());

                        if (rank != null) ctx.with(rank.placeholders());
                    });
            })
            .actionProvider(claim -> context -> this.handleClaimClick(context, claim))
            .build();
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        Player player = context.getPlayer();

        List<RegionClaim> claims = this.repository.values()
            .stream()
            .filter(claim -> claim.isOwnerOrMember(player))
            .sorted(
                Comparator.comparingInt((RegionClaim claim) -> claim.isOwner(player) ? 1 : 0)
                    .reversed()
                    .thenComparing(Comparator.comparing(claim -> claim.id().value()))
            )
            .toList();

        if (this.regionItemPopulator != null) {
            this.regionItemPopulator.populateTo(context, claims, items);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleClaimClick(ActionContext context, RegionClaim claim) {
        Player player = context.getPlayer();
        this.controller.onClaimListClick(player, claim);
    }
}
