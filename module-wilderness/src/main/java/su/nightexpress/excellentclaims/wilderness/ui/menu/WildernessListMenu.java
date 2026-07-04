package su.nightexpress.excellentclaims.wilderness.ui.menu;

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
import su.nightexpress.excellentclaims.wilderness.WildernessPlaceholders;
import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIController;
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
public class WildernessListMenu extends AbstractMenu {

    private static final String DEFAULT_TITLE = "Wilderness Regions";

    private final WildernessRepository   repository;
    private final WildernessUIController controller;

    @Nullable
    private ItemPopulator<WildernessRegion> regionItemPopulator;

    public WildernessListMenu(ClaimPlugin plugin,
                              WildernessRepository repository,
                              WildernessUIController controller) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE);
        this.repository = repository;
        this.controller = controller;
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
        String regionName = config.getOrSet("Wilderness.Name", ConfigCodecs.STRING,
            WildernessPlaceholders.WILDERNESS_NAME);

        List<String> regionLore = config.getOrSet("Wilderness.Lore.Default", ConfigCodecs.STRING_LIST, Lists.newList(
            WildernessPlaceholders.WILDERNESS_DESCRIPTION,
            CommonPlaceholders.EMPTY_IF_ABOVE,
            TagWrappers.GREEN.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to manage"))
        ));

        int[] regionSlots = config.getOrSet("Wilderness.Slots", ConfigCodecs.INT_ARRAY, IntStream.range(0, 45)
            .toArray());

        this.regionItemPopulator = ItemPopulator.builder(WildernessRegion.class)
            .slots(regionSlots)
            .itemProvider((context, claim) -> {
                return claim.getIcon()
                    .setDisplayName(regionName)
                    .setLore(regionLore)
                    .hideAllComponents()
                    .replace(ctx -> {
                        ctx.with(claim.placeholders());
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
        List<WildernessRegion> claims = this.repository.values()
            .stream()
            .sorted(Comparator.comparing(claim -> claim.id().value()))
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

    private void handleClaimClick(ActionContext context, WildernessRegion claim) {
        Player player = context.getPlayer();
        this.controller.onClaimListClick(player, claim);
    }
}
