package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.flag.Flag;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.FlagRegistry;
import su.nightexpress.excellentclaims.flag.impl.AbstractFlag;
import su.nightexpress.excellentclaims.flag.type.EntryList;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.Filled;
import su.nightexpress.nightcore.ui.menu.data.MenuFiller;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.*;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@SuppressWarnings("UnstableApiUsage")
public class FlagsMenu extends LinkedMenu<ClaimPlugin, FlagsMenu.Data> implements Filled<AbstractFlag<?>>, ConfigBased {

    public static final String FILE_NAME = "claim_flags.yml";

    private static final String ACTION = "%action%";
    private static final String UNSET = "%unset%";

    private String       flagName;
    private List<String> flagLore;
    private int[]        flagSlots;
    private Map<String, List<String>> flagActions;
    private List<String> flagUnset;

    public record Data(Claim claim, FlagCategory category){}

    public FlagsMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X5, BLACK.enclose("Claim Flags: " + CLAIM_NAME));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
    }

    public void open(@NotNull Player player, @NotNull Claim claim, @NotNull FlagCategory category) {
        this.open(player, new Data(claim, category));
    }

    @NotNull
    private String getFlagType(@NotNull AbstractFlag<?> flag) {
        Class<?> clazz = flag.getValueType();
        String type;
        if (clazz.isEnum()) {
            type = "enum";
        }
        else if (EntryList.class.isAssignableFrom(clazz)) {
            type = "list";
        }
        else type = StringUtil.lowerCaseUnderscore(flag.getValueType().getSimpleName());

        return type + "_flag";
    }

    @Override
    @NotNull
    public MenuFiller<AbstractFlag<?>> createFiller(@NotNull MenuViewer viewer) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(viewer);
        Claim claim = data.claim;
        FlagCategory category = data.category;

        return MenuFiller.builder(this)
            .setSlots(this.flagSlots)
            .setItems(FlagRegistry.getFlags(category).stream().filter(flag -> flag.isManageAvailable(claim) && flag.hasPermission(player)).sorted(Comparator.comparing(Flag::getId)).toList())
            .setItemCreator(flag -> {
                return flag.getIcon()
                    .setHideComponents(true)
                    .setDisplayName(this.flagName)
                    .setLore(this.flagLore)
                    .replacement(replacer -> replacer
                        .replace(ACTION, this.flagActions.getOrDefault(this.getFlagType(flag), Collections.emptyList()))
                        .replace(UNSET, claim.isWilderness() ? new ArrayList<>(this.flagUnset) : Collections.emptyList())
                        .replace(flag.replacePlaceholders())
                        .replace(GENERIC_VALUE, claim.hasFlag(flag) ? claim.getFlagValue(flag).getLocalized() : Lang.OTHER_UNSET.getString())
                    );
            })
            .setItemClick(flag -> (viewer1, event) -> {
                if (claim.isWilderness() && event.getClick() == ClickType.DROP) {
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
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        this.flagName = ConfigValue.create("Flag.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(FLAG_NAME))
        ).read(config);

        this.flagLore = ConfigValue.create("Flag.Lore", Lists.newList(
            GENERIC_VALUE,
            EMPTY_IF_BELOW,
            FLAG_DESCRIPTION,
            EMPTY_IF_BELOW,
            ACTION,
            UNSET
        )).read(config);

        this.flagUnset = ConfigValue.create("Flag.Unset", Lists.newList(
            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " [Q/Drop] to " + LIGHT_YELLOW.enclose("unset") + ".")
        )).read(config);

        this.flagSlots = ConfigValue.create("Flag.Slots", IntStream.range(0, 36).toArray()).read(config);

        this.flagActions = new HashMap<>();
        FlagRegistry.getFlags().forEach(flag -> {
            String type = this.getFlagType(flag);//flag.getManageType();
            List<String> info = ConfigValue.create("Flag.ActionInfo." + type, flag.getManageInfo()).read(config);
            this.flagActions.putIfAbsent(type, info);
        });

        loader.addDefaultItem(MenuItem.buildNextPage(this, 44));
        loader.addDefaultItem(MenuItem.buildPreviousPage(this, 36));
        loader.addDefaultItem(MenuItem.buildReturn(this, 40, (viewer, event) -> {
            Player player = viewer.getPlayer();
            Data data = this.getLink(viewer);

            this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(player, data.claim));
        }, ItemOptions.builder().setVisibilityPolicy(viewer -> {
                Data data = this.getLink(viewer);
                Claim claim = data.claim;

                return !claim.isWilderness() && claim.hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_CLAIM);
            })
            .build())
        );
    }
}
