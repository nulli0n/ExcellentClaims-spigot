package su.nightexpress.excellentclaims.menu.impl;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.menu.MenuViewer;
import su.nightexpress.nightcore.ui.menu.data.ConfigBased;
import su.nightexpress.nightcore.ui.menu.data.MenuLoader;
import su.nightexpress.nightcore.ui.menu.item.ItemHandler;
import su.nightexpress.nightcore.ui.menu.item.ItemOptions;
import su.nightexpress.nightcore.ui.menu.item.MenuItem;
import su.nightexpress.nightcore.ui.menu.type.LinkedMenu;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import static su.nightexpress.nightcore.util.Placeholders.*;
import static su.nightexpress.nightcore.util.text.tag.Tags.*;

@SuppressWarnings("UnstableApiUsage")
public class FlagsTypeMenu extends LinkedMenu<ClaimPlugin, Claim> implements ConfigBased {

    public static final String FILE_NAME = "flag_types.yml";

    public FlagsTypeMenu(@NotNull ClaimPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X4, BLACK.enclose("Select category..."));

        this.load(FileConfig.loadOrExtract(plugin, Config.DIR_UI, FILE_NAME));
    }

    private void onCategoryClick(@NotNull MenuViewer viewer, @NotNull FlagCategory category) {
        this.runNextTick(() -> plugin.getMenuManager().openFlagsMenu(viewer.getPlayer(), this.getLink(viewer), category));
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull InventoryView view) {

    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {

    }

    @Override
    public void loadConfiguration(@NotNull FileConfig config, @NotNull MenuLoader loader) {
        loader.addDefaultItem(NightItem.asCustomHead(SKIN_ARROW_DOWN)
            .localized(Lang.EDITOR_ITEM_RETURN)
            .toMenuItem()
            .setPriority(10)
            .setSlots(31)
            .setHandler(ItemHandler.forReturn(this, (viewer, event) -> {
                this.runNextTick(() -> plugin.getMenuManager().openClaimMenu(viewer.getPlayer(), this.getLink(viewer)));
            }))
        );

        for (FlagCategory category : FlagCategory.values()) {
            ItemHandler handler = new ItemHandler(category.name(), (viewer, event) -> {
                this.onCategoryClick(viewer, category);
            }, ItemOptions.builder()
                .setVisibilityPolicy(viewer -> viewer.getPlayer().hasPermission(Perms.FLAG_TYPE.apply(category)))
                .build()
            );

            MenuItem menuItem = (switch (category) {
                case NATURAL ->
                    NightItem.asCustomHead("89f7a04ac334fcaf618da9e841f03c00d749002dc592f8540ef9534442cecf42")
                        .setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Natural Flags")))
                        .setLore(Lists.newList(
                            LIGHT_GRAY.enclose("Flags to control natural"),
                            LIGHT_GRAY.enclose("world behavior inside a claim."),
                            "",
                            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")))
                        .toMenuItem()
                        .setPriority(10)
                        .setSlots(11)
                        .setHandler(handler);
                case PLAYER ->
                    NightItem.asCustomHead("97e6e5657b8f85f3af2c835b3533856607682f8571a4548967e2bdb535ac56b7")
                        .setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Player Flags")))
                        .setLore(Lists.newList(
                            LIGHT_GRAY.enclose("Flags to control player and"),
                            LIGHT_GRAY.enclose("non-member activity inside a claim."),
                            "",
                            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")))
                        .toMenuItem()
                        .setPriority(10)
                        .setSlots(13)
                        .setHandler(handler);
                case ENTITY ->
                    NightItem.asCustomHead("1c26ec209756ff5d5b81f25ca4db2ee4dceb52874e5f35bb98ce82cace8ac506")
                        .setDisplayName(LIGHT_YELLOW.enclose(BOLD.enclose("Entity Flags")))
                        .setLore(Lists.newList(
                            LIGHT_GRAY.enclose("Flags to control entity activity"),
                            LIGHT_GRAY.enclose("and behavior inside a claim."),
                            "",
                            LIGHT_GRAY.enclose(LIGHT_YELLOW.enclose("[▶]") + " Click to " + LIGHT_YELLOW.enclose("navigate") + ".")))
                        .toMenuItem()
                        .setPriority(10)
                        .setSlots(15)
                        .setHandler(handler);
            }).build();

            loader.addDefaultItem(menuItem);
        }
    }
}
