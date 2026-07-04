package su.nightexpress.excellentclaims.wilderness.ui.menu;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.api.menu.DynamicButtonExtension;
import su.nightexpress.excellentclaims.wilderness.WildernessPlaceholders;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class WildernessMainMenu extends AbstractObjectMenu<WildernessRegion> {

    private static final String DEFAULT_TITLE = "[%s] Menu"
        .formatted(WildernessPlaceholders.WILDERNESS_NAME);

    private final WildernessUIService uiService;

    public WildernessMainMenu(ClaimPlugin plugin, WildernessUIService uiService) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, WildernessRegion.class);
        this.uiService = uiService;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        return PlaceholderContext.builder()
            .with(this.getObject(context).placeholders())
            .build()
            .apply(super.getRawTitle(context));
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());
        this.addBackgroundItem(Material.GLASS_PANE, IntStream.range(20, 25).toArray());

        this.uiService.getExtensions().getExtensions(DefaultButtonExtension.class).forEach(ext -> {
            ext.onLayoutDefine(this);
        });
    }

    @Override
    protected void onLoad(FileConfig config) {

    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        List<DynamicButtonExtension> extensions = this.uiService.getExtensions().getExtensions(
            DynamicButtonExtension.class);

        extensions.forEach(extension -> {
            MenuItem button = extension.getItem(context);
            items.add(button);
        });
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }
}