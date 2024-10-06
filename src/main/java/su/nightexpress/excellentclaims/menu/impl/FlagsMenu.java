package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.api.AutoFill;
import su.nightexpress.nightcore.menu.api.AutoFilled;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;
import su.nightexpress.nightcore.menu.link.Linked;
import su.nightexpress.nightcore.menu.link.ViewLink;
import su.nightexpress.nightcore.util.ItemReplacer;
import su.nightexpress.nightcore.util.ItemUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.*;
import java.util.stream.IntStream;

import static su.nightexpress.excellentclaims.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class FlagsMenu extends ConfigMenu<ClaimPlugin> implements AutoFilled<AbstractFlag<?>>, Linked<FlagsMenu.Data> {

    public static final String FILE_NAME = "claim_flags.yml";

    private static final String ACTION = "%action%";

    private final ItemHandler returnHandler;
    private final ViewLink<Data> link;

    private String       flagName;
    private List<String> flagLore;
    private int[]        flagSlots;
    private Map<String, List<String>> flagActions;

    public record Data(Claim claim, FlagCategory category){}

    public FlagsMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
        this.link = new ViewLink<>();

        this.addHandler(this.returnHandler = ItemHandler.forReturn(this, (viewer, event) -> {
            Player player = viewer.getPlayer();
            Data data = this.getLink(viewer);
            Claim claim = data.claim;

            this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(player, claim));
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            if (menuItem.getHandler() == this.returnHandler) {
                menuItem.getOptions().addVisibilityPolicy(viewer -> this.getLink(viewer).claim.hasPermission(viewer.getPlayer(), ClaimPermission.MANAGE_CLAIM));
            }
        });
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
    public ViewLink<Data> getLink() {
        return this.link;
    }

    @Override
    public void onAutoFill(@NotNull MenuViewer viewer, @NotNull AutoFill<AbstractFlag<?>> autoFill) {
        Player player = viewer.getPlayer();
        Data data = this.getLink(viewer);
        Claim claim = data.claim;
        FlagCategory category = data.category;

        autoFill.setSlots(this.flagSlots);
        autoFill.setItems(FlagRegistry.getFlags(category).stream().filter(flag -> flag.isManageAvailable(claim)).sorted(Comparator.comparing(Flag::getId)).toList());
        autoFill.setItemCreator(flag -> {
            ItemStack itemStack = flag.getIcon();
            ItemReplacer.create(itemStack).trimmed().hideFlags()
                .setDisplayName(this.flagName)
                .setLore(this.flagLore)
                .replace(ACTION, this.flagActions.getOrDefault(this.getFlagType(flag), Collections.emptyList()))
                .replace(flag.replacePlaceholders())
                .replace(GENERIC_VALUE, claim.getFlagValue(flag).getLocalized())
                .writeMeta();
            return itemStack;
        });
        autoFill.setClickAction(flag -> (viewer1, event) -> {
            flag.onManageClick(this, viewer1, event, claim);
        });
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        Claim claim = this.getLink(viewer).claim;

        options.editTitle(title -> claim.replacePlaceholders().apply(title));
        this.autoFill(viewer);
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Claim Flags: " + CLAIM_NAME), MenuSize.CHEST_45);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        List<MenuItem> list = new ArrayList<>();

        ItemStack nextPage = ItemUtil.getSkinHead(SKIN_ARROW_RIGHT);
        ItemUtil.editMeta(nextPage, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_NEXT_PAGE.getLocalizedName());
        });
        list.add(new MenuItem(nextPage).setPriority(10).setSlots(44).setHandler(ItemHandler.forNextPage(this)));

        ItemStack backPage = ItemUtil.getSkinHead(SKIN_ARROW_LEFT);
        ItemUtil.editMeta(backPage, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_PREVIOUS_PAGE.getLocalizedName());
        });
        list.add(new MenuItem(backPage).setPriority(10).setSlots(36).setHandler(ItemHandler.forPreviousPage(this)));

        ItemStack returnItem = ItemUtil.getSkinHead(SKIN_ARROW_DOWN);
        ItemUtil.editMeta(returnItem, meta -> {
            meta.setDisplayName(Lang.EDITOR_ITEM_RETURN.getLocalizedName());
        });
        list.add(new MenuItem(returnItem).setPriority(10).setSlots(40).setHandler(this.returnHandler));

        return list;
    }

    @Override
    protected void loadAdditional() {
        this.flagName = ConfigValue.create("Flag.Name",
            LIGHT_YELLOW.enclose(BOLD.enclose(FLAG_NAME))
        ).read(cfg);

        this.flagLore = ConfigValue.create("Flag.Lore", Lists.newList(
            GENERIC_VALUE,
            "",
            FLAG_DESCRIPTION,
            "",
            ACTION
        )).read(cfg);

        this.flagSlots = ConfigValue.create("Flag.Slots", IntStream.range(0, 36).toArray()).read(cfg);

        this.flagActions = new HashMap<>();
        FlagRegistry.getFlags().forEach(flag -> {
            String type = this.getFlagType(flag);//flag.getManageType();
            List<String> info = ConfigValue.create("Flag.ActionInfo." + type, flag.getManageInfo()).read(cfg);
            this.flagActions.putIfAbsent(type, info);
        });
    }
}
