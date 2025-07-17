package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.flag.FlagRegistry;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.excellentclaims.menu.type.ClaimMenu;
import su.nightexpress.excellentclaims.util.list.SmartList;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
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
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.tag.impl.ColorTag;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class FlagsMenu extends LinkedMenu<ClaimPlugin, FlagsMenu.Data> implements Filled<ClaimFlag<?>>, ConfigBased, ClaimMenu {

    private String       flagName;
    private List<String> flagLore;
    private int[]        flagSlots;

    public record Data(@NotNull Claim claim, FlagCategory category, boolean advanced){}

    public FlagsMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X6, BLACK.wrap("Claim Flags: " + CLAIM_NAME));
    }

//    @Override
//    @NotNull
//    public ClaimPermission getPermission() {
//        return ClaimPermission.MANAGE_FLAGS;
//    }

    public void open(@NotNull Player player, @NotNull Claim claim) {
        this.open(player, claim, null, false);
    }

    @Override
    public boolean hasPermission(@NotNull Player player, @NotNull Claim claim) {
        return claim.hasPermission(player, ClaimPermission.MANAGE_FLAGS);
    }

    private void open(@NotNull Player player, @NotNull Claim claim, @NotNull FlagCategory category) {
        this.open(player, claim, category, false);
    }

    private void open(@NotNull Player player, @NotNull Claim claim, @Nullable FlagCategory category, boolean advanced) {
        this.open(player, new Data(claim, category, advanced));
    }

    @Override
    @NotNull
    public MenuFiller<ClaimFlag<?>> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(viewer);
        Claim claim = data.claim;
        FlagCategory category = data.category;

        return MenuFiller.builder(this)
            .setSlots(this.flagSlots)
            .setItems(FlagRegistry.getFlags(category).stream()
                .filter(flag -> flag.hasPermission(player))
                .sorted(Comparator.comparing(Flag::getId))
                .toList())
            .setItemCreator(flag -> {
                return flag.getIcon()
                    .hideAllComponents()
                    .setDisplayName(this.flagName)
                    .setLore(this.flagLore)
                    .replacement(replacer -> replacer
                        .replace(flag.replacePlaceholders())
                        .replace(GENERIC_VALUE, () -> flag.getValueLocalized(claim))
                    );
            })
            .setItemClick(flag -> (viewer1, event) -> {
                if (event.getClick() == ClickType.DROP) {
                    claim.getFlags().remove(flag.getId());
                    claim.setSaveRequired(true);
                    this.runNextTick(() -> this.flush(viewer1));
                    return;
                }

                flag.onManageClick(this, viewer1, event, claim);
            })
            .build();
    }

    @Override
    @NotNull
    protected String getTitle(@NotNull MenuViewer viewer) {
        return this.getLink(viewer).claim.replacePlaceholders().apply(this.title);
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {
        if (this.getLink(viewer).category != null) {
            this.autoFill(viewer);
        }
        else {
            viewer.setPage(1);
            viewer.setPages(1);
        }
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    private void handleAdvancedSettings(@NotNull MenuViewer viewer) {
        this.runNextTick(() -> this.open(viewer.getPlayer(), this.getLink(viewer).claim, null, true));
    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.flagName = ConfigValue.create("Flag.Name",
            LIGHT_YELLOW.wrap(BOLD.wrap(FLAG_NAME))
        ).read(config);

        this.flagLore = ConfigValue.create("Flag.Lore", Lists.newList(
            GRAY.wrap(WHITE.wrap("✔") + " Current: " + WHITE.wrap(GENERIC_VALUE)),
            EMPTY_IF_BELOW,
            FLAG_DESCRIPTION,
            "",
            LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to change"))
        )).read(config);

        this.flagSlots = ConfigValue.create("Flag.Slots",
            new int[]{2,3,4,5,6,7,8, 11,12,13,14,15,16,17, 20,21,22,23,24,25,26, 29,30,31,32,33,34,35, 38,39,40,41,42,43,44}
        ).read(config);

        loader.addDefaultItem(MenuItem.buildReturn(this, 50, (viewer, event) -> {
            Player player = viewer.getPlayer();
            Data data = this.getLink(viewer);

            this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(player, data.claim));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> {
                Data data = this.getLink(viewer);
                Claim claim = data.claim;

                return !claim.isWilderness() && claim.hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_CLAIM);
            })
            .build())
        );

        loader.addDefaultItem(NightItem.fromType(Material.BLACK_STAINED_GLASS_PANE)
            .setHideTooltip(true)
            .toMenuItem()
            .setPriority(-1)
            .setSlots(1,10,19,27,28,36,37,46,45,47,48,49,50,51,52,53)
        );

        this.loadCategoryButtons(loader);
        this.loadAdvancedButtons(loader);
    }

    private void loadCategoryButtons(@NotNull MenuLoader loader) {
        int startSlot = 0;
        for (FlagCategory category : FlagCategory.values()) {
            loader.addDefaultItem(this.createCategoryIcon(category).toMenuItem()
                .setPriority(10)
                .setSlots(startSlot)
                .setHandler(this.createCategoryHandler(category))
            );

            startSlot += 9;
        }

        loader.addDefaultItem(MenuItem.buildNextPage(this, 53));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 47));

        loader.addDefaultItem(NightItem.fromType(Material.KNOWLEDGE_BOOK)
            .setDisplayName(GREEN.wrap(BOLD.wrap("Flag Reset")))
            .setLore(Lists.newList(
                GRAY.wrap("Press " + GREEN.wrap("[Q] Key") + " on any flag"),
                GRAY.wrap("to reset it to default state.")
            ))
            .toMenuItem()
            .setSlots(45)
            .setPriority(10)
        );
    }

    private void loadAdvancedButtons(@NotNull MenuLoader loader) {
        loader.addDefaultItem(NightItem.fromType(Material.MAGMA_CREAM)
            .setDisplayName(LIGHT_GREEN.wrap(BOLD.wrap("Advanced Settings")))
            .setLore(Lists.newList(
                GRAY.wrap("Control mob spawns, item usage"),
                GRAY.wrap("block interactions, damage types"),
                GRAY.wrap("and more!"),
                "",
                LIGHT_GREEN.wrap("→ " + UNDERLINED.wrap("Click to open"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(27)
            .setHandler(new ItemHandler("advanced_settings", (viewer, event) -> this.handleAdvancedSettings(viewer),
                ItemOptions.builder()
                    .setVisibilityPolicy(viewer -> viewer.getPlayer().hasPermission(Perms.ADVANCED_SETTINGS))
                    .build()
            ))
        );

        loader.addDefaultItem(NightItem.fromType(Material.CRAFTING_TABLE)
            .setDisplayName(LIGHT_ORANGE.wrap(BOLD.wrap("Block Interactions")))
            .setLore(Lists.newList(
                LIGHT_ORANGE.wrap("➥") + " " + GRAY.wrap("Mode: ") + LIGHT_ORANGE.wrap(GENERIC_MODE),
                LIGHT_ORANGE.wrap("➥") + " " + GRAY.wrap("Blocks Added: ") + LIGHT_ORANGE.wrap(GENERIC_AMOUNT),
                "",
                GRAY.wrap("Controls which blocks are interactable"),
                GRAY.wrap("in this claim based on mode set."),
                "",
                LIGHT_RED.wrap("This setting can be overridden by"),
                LIGHT_RED.wrap("explicit block interaction flags."),
                "",
                LIGHT_ORANGE.wrap("→ " + UNDERLINED.wrap("Click to edit"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(12)
            .setPriority(10)
            .setHandler(this.createAdvancedHandler("block_interact", Claim::getBlockUsageList, Perms.ADVANCED_BLOCK_USAGE))
        );

        loader.addDefaultItem(NightItem.fromType(Material.ARMOR_STAND)
            .setDisplayName(LIGHT_YELLOW.wrap(BOLD.wrap("Entity Interactions")))
            .setLore(Lists.newList(
                LIGHT_YELLOW.wrap("➥") + " " + GRAY.wrap("Mode: ") + LIGHT_YELLOW.wrap(GENERIC_MODE),
                LIGHT_YELLOW.wrap("➥") + " " + GRAY.wrap("Entities Added: ") + LIGHT_YELLOW.wrap(GENERIC_AMOUNT),
                "",
                GRAY.wrap("Controls which entities are interactable"),
                GRAY.wrap("in this claim based on mode set."),
                "",
                LIGHT_RED.wrap("This setting can be overridden by"),
                LIGHT_RED.wrap("explicit mob interaction flags."),
                "",
                LIGHT_YELLOW.wrap("→ " + UNDERLINED.wrap("Click to edit"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(14)
            .setPriority(10)
            .setHandler(this.createAdvancedHandler("entity_interact", Claim::getMobInteractList, Perms.ADVANCED_MOB_INTERACT))
        );

        loader.addDefaultItem(NightItem.fromType(Material.CREEPER_SPAWN_EGG)
            .setDisplayName(GREEN.wrap(BOLD.wrap("Mob Spawns")))
            .setLore(Lists.newList(
                GREEN.wrap("➥") + " " + GRAY.wrap("Mode: ") + GREEN.wrap(GENERIC_MODE),
                GREEN.wrap("➥") + " " + GRAY.wrap("Mobs Added: ") + GREEN.wrap(GENERIC_AMOUNT),
                "",
                GRAY.wrap("Controls which mob types can spawn"),
                GRAY.wrap("in this claim based on mode set."),
                "",
                LIGHT_RED.wrap("This setting can be overridden by"),
                LIGHT_RED.wrap("explicit mob spawn flags."),
                "",
                GREEN.wrap("→ " + UNDERLINED.wrap("Click to edit"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(16)
            .setPriority(10)
            .setHandler(this.createAdvancedHandler("mob_spawns", Claim::getMobSpawnList, Perms.ADVANCED_MOB_SPAWNS))
        );

        loader.addDefaultItem(NightItem.fromType(Material.PIG_SPAWN_EGG)
            .setDisplayName(LIGHT_PINK.wrap(BOLD.wrap("Animal Damage")))
            .setLore(Lists.newList(
                LIGHT_PINK.wrap("➥") + " " + GRAY.wrap("Mode: ") + LIGHT_PINK.wrap(GENERIC_MODE),
                LIGHT_PINK.wrap("➥") + " " + GRAY.wrap("Damage Types: ") + LIGHT_PINK.wrap(GENERIC_AMOUNT),
                "",
                GRAY.wrap("Controls which damage is applicable to animals"),
                GRAY.wrap("in this claim based on mode set."),
                "",
                LIGHT_RED.wrap("This setting can be overridden by"),
                LIGHT_RED.wrap("explicit damage flags."),
                "",
                LIGHT_PINK.wrap("→ " + UNDERLINED.wrap("Click to edit"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(30)
            .setPriority(10)
            .setHandler(this.createAdvancedHandler("animals_damage", Claim::getAnimalDamageList, Perms.ADVANCED_ANIMAL_DAMAGE))
        );

        loader.addDefaultItem(NightItem.fromType(Material.COMMAND_BLOCK)
            .setDisplayName(LIGHT_ORANGE.wrap(BOLD.wrap("Command Usage")))
            .setLore(Lists.newList(
                LIGHT_ORANGE.wrap("➥") + " " + GRAY.wrap("Mode: ") + LIGHT_ORANGE.wrap(GENERIC_MODE),
                LIGHT_ORANGE.wrap("➥") + " " + GRAY.wrap("Commands: ") + LIGHT_ORANGE.wrap(GENERIC_AMOUNT),
                "",
                GRAY.wrap("Controls which commands can be used"),
                GRAY.wrap("in this claim based on mode set."),
                "",
                LIGHT_RED.wrap("This setting can be overridden by"),
                LIGHT_RED.wrap("explicit player command flags."),
                "",
                LIGHT_ORANGE.wrap("→ " + UNDERLINED.wrap("Click to edit"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(32)
            .setPriority(10)
            .setHandler(this.createAdvancedHandler("command_usage", Claim::getCommandUsageList, Perms.ADVANCED_COMMAND_USAGE))
        );

        loader.addDefaultItem(NightItem.fromType(Material.PLAYER_HEAD)
            .setDisplayName(WHITE.wrap(BOLD.wrap("Player Damage")))
            .setLore(Lists.newList(
                WHITE.wrap("➥") + " " + GRAY.wrap("Mode: ") + WHITE.wrap(GENERIC_MODE),
                WHITE.wrap("➥") + " " + GRAY.wrap("Damage Types: ") + WHITE.wrap(GENERIC_AMOUNT),
                "",
                GRAY.wrap("Controls which damage is applicable to players"),
                GRAY.wrap("in this claim based on mode set."),
                "",
                LIGHT_RED.wrap("This setting can be overridden by"),
                LIGHT_RED.wrap("explicit player damage flags."),
                "",
                WHITE.wrap("→ " + UNDERLINED.wrap("Click to edit"))
            ))
            .hideAllComponents()
            .toMenuItem()
            .setSlots(34)
            .setPriority(10)
            .setHandler(this.createAdvancedHandler("player_damage", Claim::getPlayerDamageList, Perms.ADVANCED_PLAYER_DAMAGE))
        );
    }

    @NotNull
    private NightItem createCategoryIcon(@NotNull FlagCategory category) {
        String type = StringUtil.capitalizeUnderscored(category.name());

        Material material = null;
        List<String> description = null;
        ColorTag colorTag = null;

        switch (category) {
            case ENTITY -> {
                colorTag = LIGHT_PINK;
                material = Material.PIG_SPAWN_EGG;
                description = Lists.newList("Flags controls entity activity.");
            }
            case PLAYER -> {
                colorTag = LIGHT_ORANGE;
                material = Material.PLAYER_HEAD;
                description = Lists.newList("Flags controls player activity.");
            }
            case NATURAL -> {
                colorTag = LIGHT_YELLOW;
                material = Material.WHEAT;
                description = Lists.newList("Flags controls world activity.");
            }
        }

        String name = colorTag.wrap(BOLD.wrap(type + " Flags"));
        List<String> lore = Lists.modify(description, GRAY::wrap);
        lore.add("");
        lore.add(colorTag.wrap("→ " + UNDERLINED.wrap("Click to open")));

        return NightItem.fromType(material).setDisplayName(name).setLore(lore);
    }

    @NotNull
    private ItemHandler createCategoryHandler(@NotNull FlagCategory category) {
        String name = category.name().toLowerCase();

        ItemClick click = (viewer, event) -> this.runNextTick(() -> this.open(viewer.getPlayer(), this.getLink(viewer).claim, category));

        ItemOptions options = ItemOptions.builder()
            .setVisibilityPolicy(viewer -> viewer.getPlayer().hasPermission(Perms.FLAG_TYPE.apply(category)))
            .build();

        return new ItemHandler(name, click, options);
    }

    @NotNull
    private <T> ItemHandler createAdvancedHandler(@NotNull String name, @NotNull Function<Claim, SmartList<T>> function, @NotNull Permission permission) {
        ItemClick click = (viewer, event) -> {
            Player player = viewer.getPlayer();
            Claim claim = this.getLink(player).claim;
            SmartList<T> list = function.apply(claim);

            this.runNextTick(() -> plugin.getMenuManager().openEntrySelection(player, claim, list));
        };

        ItemOptions options = ItemOptions.builder()
            .setVisibilityPolicy(viewer -> this.getLink(viewer).advanced && viewer.getPlayer().hasPermission(permission))
            .setDisplayModifier((viewer, item) -> {
                SmartList<T> list = function.apply(this.getLink(viewer).claim);

                item.replacement(replacer -> replacer
                    .replace(GENERIC_AMOUNT, () -> NumberUtil.format(list.countEntries()))
                    .replace(GENERIC_MODE, () -> Lang.LIST_MODE.getLocalized(list.getMode()))
                );
            })
            .build();

        return new ItemHandler(name, click, options);
    }
}
