package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.util.list.SmartEntry;
import su.nightexpress.excellentclaims.util.list.SmartList;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemClick;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class EntrySelectionMenu extends LinkedMenu<ClaimPlugin, EntrySelectionMenu.Data> implements Filled<SmartEntry<?>>, ConfigBased {

    public record Data(@NotNull Claim claim, @NotNull SmartList<?> list, @NotNull FilterType filterType, @Nullable String search){}

    private String entryNameEnabled;
    private String entryNameDisabled;
    private List<String> entryLoreEnabled;
    private List<String> entryLoreDisabled;
    private int[] entrySlots;

    private enum FilterType {
        NONE((entry, smartList) -> true),
        ENABLED((entry, smartList) -> smartList.containsEntry(entry.getName())),
        DISABLED(ENABLED.predicate.negate());

        private final BiPredicate<SmartEntry<?>, SmartList<?>> predicate;

        FilterType(@NotNull BiPredicate<SmartEntry<?>, SmartList<?>> predicate) {
            this.predicate = predicate;
        }
    }

    public EntrySelectionMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, BLACK.wrap("Entry Selector"));
    }

    public <T> void open(@NotNull Player player, @NotNull Claim claim, @NotNull SmartList<T> list) {
        this.open(player, claim, list, FilterType.NONE, null);
    }

    private <T> void open(@NotNull Player player, @NotNull Claim claim, @NotNull SmartList<T> list, @NotNull FilterType type, @Nullable String search) {
        this.open(player, new Data(claim, list, type, search));
    }

    @Override
    @NotNull
    public MenuFiller<SmartEntry<?>> createFiller(@NotNull MenuViewer viewer) {
        Data data = this.getLink(viewer);
        Claim claim = data.claim;
        FilterType filter = data.filterType;
        SmartList<?> list = data.list;
        Set<SmartEntry<?>> entries = new HashSet<>(list.getType().getAllValues());
        Predicate<SmartEntry<?>> searchTest = entry -> data.search == null || entry.getLocalizedName().contains(data.search);

        String modeLang = Lang.LIST_MODE.getLocalized(list.getMode());

        return MenuFiller.builder(this)
            .setSlots(this.entrySlots)
            .setItems(entries.stream().filter(entry -> searchTest.test(entry) && filter.predicate.test(entry, list)).sorted(Comparator.comparing(SmartEntry::getName)).toList())
            .setItemCreator(entry -> {
                boolean isEnabled = list.containsEntry(entry.getName());

                return entry.getIcon()
                    .hideAllComponents()
                    .setDisplayName(isEnabled ? this.entryNameEnabled : this.entryNameDisabled)
                    .setLore(isEnabled ? this.entryLoreEnabled : this.entryLoreDisabled)
                    .replacement(replacer -> replacer
                        .replace(GENERIC_MODE, () -> modeLang)
                        .replace(GENERIC_NAME, entry::getLocalizedName)
                    );
            })
            .setItemClick(entry -> (viewer1, event) -> {
                String name = entry.getName();

                if (list.containsEntry(name)) {
                    list.removeEntry(name);
                }
                else {
                    list.addEntry(name);
                }

                claim.setSaveRequired(true);
                this.runNextTick(() -> this.flush(viewer));
            })
            .build();
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    protected void onItemPrepare(@NotNull MenuViewer viewer, @NotNull MenuItem menuItem, @NotNull NightItem item) {
        super.onItemPrepare(viewer, menuItem, item);

        if (viewer.hasItem(menuItem)) return;

        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        SmartList<?> list = data.list;

        item.replacement(replacer -> replacer.replace(GENERIC_MODE, Lang.LIST_MODE.getLocalized(list.getMode())));
    }

    private void handleReturn(@NotNull MenuViewer viewer) {
        this.runNextTick(() -> this.plugin.getMenuManager().openFlagsMenu(viewer.getPlayer(), this.getLink(viewer).claim));
    }

    private void handleListMode(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        SmartList<?> list = data.list;

        list.setMode(Lists.next(list.getMode()));
        data.claim.setSaveRequired(true);

        this.runNextTick(() -> this.flush(viewer));
    }

    private void handleListClear(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);

        data.list.clear();
        data.claim.setSaveRequired(true);

        this.runNextTick(() -> this.flush(viewer));
    }

    private void handleSearch(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);

        this.handleInput(Dialog.builder(player, input -> {
            String search = input.getText();
            this.open(player, data.claim, data.list, data.filterType, search);
            return true;
        }));
    }

    private void handleSearchClear(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(player);
        this.runNextTick(() -> this.open(player, data.claim, data.list, data.filterType, null));
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.entryNameEnabled = ConfigValue.create("Entry.Name.Enabled", GREEN.wrap("✔") + " " + WHITE.wrap(GENERIC_NAME)).read(config);
        this.entryNameDisabled = ConfigValue.create("Entry.Name.Disabled", RED.wrap("✘") + " " + WHITE.wrap(GENERIC_NAME)).read(config);

        this.entryLoreEnabled = ConfigValue.create("Entry.Lore.Enabled", Lists.newList(
            GRAY.wrap("This entry is in " + GREEN.wrap(GENERIC_MODE) + "."),
            "",
            GREEN.wrap("→ " + UNDERLINED.wrap("Click to remove"))
        )).read(config);

        this.entryLoreDisabled = ConfigValue.create("Entry.Lore.Disabled", Lists.newList(
            GRAY.wrap("This entry is not in " + RED.wrap(GENERIC_MODE) + "."),
            "",
            RED.wrap("→ " + UNDERLINED.wrap("Click to add"))
        )).read(config);

        this.entrySlots = ConfigValue.create("Entry.DisplaySlots", new int[]{10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34}).read(config);

        loader.addDefaultItem(NightItem.fromType(Material.COMPARATOR)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Mode")))
            .setLore(Lists.newList(
                LIGHT_YELLOW.wrap("➥") + " " + GRAY.wrap("Current: ") + LIGHT_YELLOW.wrap(GENERIC_MODE),
                "",
                GRAY.wrap("Use " + WHITE.wrap("whitelist") + " to allow"),
                GRAY.wrap("only specific entries."),
                "",
                GRAY.wrap("Use " + WHITE.wrap("blacklist") + " to ban"),
                GRAY.wrap("only specific entries."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to toggle"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(47)
            .setPriority(10)
            .setHandler(new ItemHandler("list_mode", (viewer, event) -> this.handleListMode(viewer)))
        );

        loader.addDefaultItem(NightItem.fromType(Material.COMPASS)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Search")))
            .setLore(Lists.newList(
                GRAY.wrap("Search for specific entries."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to search"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(4)
            .setPriority(10)
            .setHandler(new ItemHandler("new_search", (viewer, event) -> this.handleSearch(viewer),
                ItemOptions.builder()
                    .setVisibilityPolicy(viewer -> this.getLink(viewer).search == null)
                    .build()
            ))
        );

        loader.addDefaultItem(NightItem.fromType(Material.RECOVERY_COMPASS)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Clear Search")))
            .setLore(Lists.newList(
                GRAY.wrap("Clear search results."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to clear"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(4)
            .setPriority(10)
            .setHandler(new ItemHandler("clear_search", (viewer, event) -> this.handleSearchClear(viewer),
                ItemOptions.builder()
                    .setVisibilityPolicy(viewer -> this.getLink(viewer).search != null)
                    .build()
            ))
        );

        loader.addDefaultItem(NightItem.fromType(Material.MILK_BUCKET)
            .setDisplayName(WHITE.wrap(BOLD.wrap("Clear")))
            .setLore(Lists.newList(
                GRAY.wrap("Removes all entries from the " + WHITE.wrap(GENERIC_MODE) + "."),
                "",
                WHITE.wrap("→ " + UNDERLINED.wrap("Click to clear"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(51)
            .setPriority(10)
            .setHandler(new ItemHandler("list_clear", (viewer, event) -> this.handleListClear(viewer)))
        );

        loader.addDefaultItem(NightItem.fromType(Material.LIME_STAINED_GLASS_PANE)
            .setDisplayName(GREEN.wrap(BOLD.wrap("Filter Enabled")))
            .setLore(Lists.newList(
                GRAY.wrap("Shows only enabled entries."),
                "",
                GREEN.wrap("→ " + UNDERLINED.wrap("Click to toggle"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(45)
            .setPriority(10)
            .setHandler(this.createHandler("filter_enabled", FilterType.ENABLED))
        );

        loader.addDefaultItem(NightItem.fromType(Material.RED_STAINED_GLASS_PANE)
            .setDisplayName(RED.wrap(BOLD.wrap("Filter Disabled")))
            .setLore(Lists.newList(
                GRAY.wrap("Shows only " + RED.wrap("disabled") + " entries."),
                "",
                RED.wrap("→ " + UNDERLINED.wrap("Click to toggle"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(53)
            .setPriority(10)
            .setHandler(this.createHandler("filter_disabled", FilterType.DISABLED))
        );

        loader.addDefaultItem(NightItem.fromType(Material.WHITE_STAINED_GLASS_PANE)
            .setDisplayName(WHITE.wrap(BOLD.wrap("Clear Filter")))
            .setLore(Lists.newList(
                GRAY.wrap("Shows all available entries."),
                "",
                WHITE.wrap("→ " + UNDERLINED.wrap("Click to toggle"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(45,53)
            .setPriority(5)
            .setHandler(this.createHandler("filter_none", FilterType.NONE))
        );

        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE).setHideTooltip(true).toMenuItem().setSlots(IntStream.range(45, 54).toArray()));

        loader.addDefaultItem(MenuItem.buildReturn(this, 49, (viewer, event) -> this.handleReturn(viewer)).setPriority(10));
        loader.addDefaultItem(MenuItem.buildNextPage(this, 26));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 18));
    }

    @NotNull
    private ItemHandler createHandler(@NotNull String name, @NotNull FilterType type) {
        ItemClick click = (viewer, event) -> {
            Player player = viewer.getPlayer();
            Data data = this.getLink(player);

            this.runNextTick(() -> this.open(player, data.claim, data.list, type, data.search));
        };

        ItemOptions options = ItemOptions.builder()
            .setVisibilityPolicy(viewer -> this.getLink(viewer).filterType != type)
            .build();

        return new ItemHandler(name, click, options);
    }
}
