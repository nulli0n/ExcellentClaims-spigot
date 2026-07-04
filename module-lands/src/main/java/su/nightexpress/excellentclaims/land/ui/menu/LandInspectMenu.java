package su.nightexpress.excellentclaims.land.ui.menu;

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
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RanksAPI;
import su.nightexpress.excellentclaims.land.LandsPlaceholders;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.ui.LandUIController;
import su.nightexpress.excellentclaims.land.ui.context.InspectContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.userdata.UserData;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class LandInspectMenu extends AbstractObjectMenu<InspectContext> {

    private static final String DEFAULT_TITLE = "[%s] Land Claims"
        .formatted(CommonPlaceholders.PLAYER_NAME);

    private final LandUIController   controller;
    private final ClaimPermissionAPI permissions;

    @Nullable
    private final RanksAPI ranksAPI;

    @Nullable
    private ItemPopulator<LandClaim> landItemPopulator;

    public LandInspectMenu(ClaimPlugin plugin,
                           LandUIController controller,
                           ClaimPermissionAPI permissions,
                           @Nullable RanksAPI ranksAPI) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, InspectContext.class);
        this.controller = controller;
        this.permissions = permissions;
        this.ranksAPI = ranksAPI;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        InspectContext inspectContext = this.getObject(context);

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(CommonPlaceholders.PLAYER_NAME, () -> inspectContext.user().getName())
            .build();

        return placeholders.apply(super.getRawTitle(context));
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
        String landName = config.getOrSet("Land.Name", ConfigCodecs.STRING, LandsPlaceholders.LAND_NAME);

        List<String> landLoreAdmin = config.getOrSet("Land.Lore.Admin", ConfigCodecs.STRING_LIST, Lists.newList(
            LandsPlaceholders.LAND_DESCRIPTION,
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Rank: ") + ClaimsPlaceholders.RANK_NAME),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("ID: ") + LandsPlaceholders.LAND_ID),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("World: ") + LandsPlaceholders.LAND_WORLD),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Priority: ") + LandsPlaceholders.LAND_PRIORITY),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Members: ") + LandsPlaceholders.LAND_MEMBERS),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Chunks: ") + LandsPlaceholders.LAND_SIZE),
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.RED.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to edit"))
        ));

        List<String> landLoreViewer = config.getOrSet("Land.Lore.Viewer", ConfigCodecs.STRING_LIST, Lists.newList(
            LandsPlaceholders.LAND_DESCRIPTION,
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Rank: ") + ClaimsPlaceholders.RANK_NAME),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("World: ") + LandsPlaceholders.LAND_WORLD),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Members: ") + LandsPlaceholders.LAND_MEMBERS),
            TagWrappers.GOLD.wrap("» " + TagWrappers.GRAY.wrap("Chunks: ") + LandsPlaceholders.LAND_SIZE)
        ));

        int[] landSlots = config.getOrSet("Land.Slots", ConfigCodecs.INT_ARRAY, IntStream.range(0, 45).toArray());

        this.landItemPopulator = ItemPopulator.builder(LandClaim.class)
            .slots(landSlots)
            .itemProvider((context, claim) -> {
                Player player = context.getPlayer();
                InspectContext inspectContext = this.getObject(context);
                UserData user = inspectContext.user();

                Rank rank = this.ranksAPI == null ? null : this.ranksAPI.resolveRank(claim, user.getId());
                boolean isAdmin = this.permissions.hasPermission(player, claim, ClaimPermission.MANAGE_CLAIM);

                return claim.getIcon()
                    .setDisplayName(landName)
                    .setLore(isAdmin ? landLoreAdmin : landLoreViewer)
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
        InspectContext inspectContext = this.getObject(context);
        List<LandClaim> claims = inspectContext.claims();

        if (this.landItemPopulator != null) {
            this.landItemPopulator.populateTo(context, claims, items);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleClaimClick(ActionContext context, LandClaim claim) {
        Player player = context.getPlayer();
        InspectContext inspectContext = this.getObject(context);

        this.controller.onClaimInspectClick(player, claim, inspectContext);
    }
}
