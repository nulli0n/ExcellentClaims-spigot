package su.nightexpress.excellentclaims.rules.ui.menu;

import static su.nightexpress.nightcore.util.placeholder.CommonPlaceholders.EMPTY_IF_BELOW;
import static su.nightexpress.nightcore.util.placeholder.CommonPlaceholders.GENERIC_VALUE;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.BOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.DARK_GRAY;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GOLD;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GRAY;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.GREEN;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.ORANGE;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.RED;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.UNDERLINED;
import static su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers.WHITE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.rules.RuleRegistry;
import su.nightexpress.excellentclaims.rules.RulesPlaceholders;
import su.nightexpress.excellentclaims.rules.ui.RuleUIController;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.ConfigTypes;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.action.ObjectActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.LowerCase;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RuleListMenu extends AbstractObjectMenu<RuleListContext> {

    private static final String DEFAULT_TITLE = "[%s] Properties - %s"
        .formatted(CommonPlaceholders.GENERIC_NAME, ClaimsPlaceholders.GENERIC_TYPE);

    private final RuleRegistry     rules;
    private final RuleUIController controller;

    @Nullable
    private ItemPopulator<ClaimRule<?>> propertyPopulator;

    public RuleListMenu(ClaimPlugin plugin, RuleRegistry rules, RuleUIController controller) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, RuleListContext.class);
        this.rules = rules;
        this.controller = controller;
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        RuleListContext listContext = this.getObject(context);

        return PlaceholderContext.builder()
            .with(listContext.claim().placeholders())
            .with(CommonPlaceholders.GENERIC_NAME, () -> listContext.claim().getRawName())
            .with(ClaimsPlaceholders.GENERIC_TYPE, () -> Optional.ofNullable(listContext.category())
                .map(Lang.RULE_CATEGORY::getLocalized)
                .orElse(Lang.OTHER_EVERYTHING.text()))
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
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(0, 9).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(9, 45).toArray());

        this.addNextPageButton(53);
        this.addPreviousPageButton(45);

        this.addDefaultButton("return", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_EYE)
                    .setDisplayName(GREEN.and(BOLD).wrap("Back to Settings"))
                    .hideAllComponents()
                )
                .action(this.createObjectAction(this::handleBackClick))
                .build()
            )
            .slots(49)
            .build()
        );

        this.loadCategoryButtons();
    }

    @Override
    protected void onLoad(FileConfig config) {
        String flagName = config.get(ConfigTypes.STRING, "Flag.Name", GOLD.wrap(BOLD.wrap(
            RulesPlaceholders.RULE_NAME)));

        List<String> flagLore = config.get(ConfigTypes.STRING_LIST, "Flag.Lore", Lists.newList(
            DARK_GRAY.wrap("»" + GRAY.wrap(" Current: ") + WHITE.wrap(GENERIC_VALUE)),
            EMPTY_IF_BELOW,
            RulesPlaceholders.RULE_DESCRIPTION,
            "",
            GOLD.wrap("→ " + UNDERLINED.wrap("Click to edit"))
        ));

        int[] flagSlots = config.get(ConfigTypes.INT_ARRAY, "Flag.Slots", IntStream.range(9, 45).toArray());

        this.propertyPopulator = ItemPopulator.<ClaimRule<?>>builder()
            .slots(flagSlots)
            .itemProvider((context, property) -> {
                Claim claim = this.getObject(context).claim();

                return property.getIcon()
                    .hideAllComponents()
                    .setDisplayName(flagName)
                    .setLore(flagLore)
                    .replace(builder -> builder
                        .with(property.placeholders())
                        .with(GENERIC_VALUE, () -> claim.formatSummary(property))
                    );
            })
            .actionProvider(property -> context -> {
                if (context.getEvent().getClick() == ClickType.DROP) {
                    RuleListContext listContext = this.getObject(context);
                    Claim claim = listContext.claim();
                    ClaimRules properties = claim.getRules();
                    properties.unset(property);

                    listContext.markDirty();
                    context.getViewer().refresh();
                    return;
                }

                this.handleRuleClick(context, property);
            })
            .build();

    }

    private void loadCategoryButtons() {
        for (RuleCategory category : RuleCategory.values()) {

            String id = "category_" + LowerCase.INTERNAL.apply(category.name());

            int slot = switch (category) {
                case PLAYER -> 1;
                case ENTITY -> 4;
                case NATURAL -> 7;
            };

            NightItem normalIcon = switch (category) {
                case NATURAL -> NightItem.fromType(Material.DEAD_BUSH).setDisplayName(GOLD.and(BOLD).wrap(
                    "Environment"));
                case ENTITY -> NightItem.fromType(Material.EGG).setDisplayName(RED.and(BOLD).wrap("Mobs & Entities"));
                case PLAYER -> NightItem.asCustomHead(
                    "7fa5148e821c1fbe23d63d08776b055b0654028e2e3965ec0dc48b4e62ce6d05").setDisplayName(WHITE.and(BOLD)
                        .wrap("Player"));
            };

            NightItem selectedIcon = switch (category) {
                case NATURAL -> NightItem.fromType(Material.OAK_SAPLING).setDisplayName(GREEN.and(BOLD).wrap(
                    "Environment")).setEnchantGlint(true);
                case ENTITY -> NightItem.fromType(Material.CREEPER_SPAWN_EGG).setDisplayName(GREEN.and(BOLD).wrap(
                    "Mobs & Entities")).setEnchantGlint(true);
                case PLAYER -> NightItem.asCustomHead("a5e81602e019b8f6c4a59bc3a31c890f7aa640bf65e9b5bd10950fa590a35d5")
                    .setDisplayName(ORANGE.and(BOLD).wrap("Player")).setEnchantGlint(true);
            };

            this.addDefaultButton(id, MenuItem.builder()
                .defaultState(ItemState.builder()
                    .icon(normalIcon.hideAllComponents())
                    .action(this.createObjectAction(context -> this.handleCategoryClick(context, category)))
                    .build()
                )
                .state("selected", ItemState.builder()
                    .icon(selectedIcon.hideAllComponents())
                    .action(this.createObjectAction(context -> this.handleCategoryClick(context, category)))
                    .condition(context -> this.getObject(context).category() == category)
                    .build()
                )
                .slots(slot)
                .build()
            );
        }

        /*loader.addDefaultItem(NightItem.fromType(Material.KNOWLEDGE_BOOK)
            .setDisplayName(GREEN.wrap(BOLD.wrap("Flag Reset")))
            .setLore(Lists.newList(
                GRAY.wrap("Press " + GREEN.wrap("[Q] Key") + " on any flag"),
                GRAY.wrap("to reset it to default state.")
            ))
            .toMenuItem()
            .setSlots(45)
            .setPriority(10)
        );*/
    }

    private void handleBackClick(ObjectActionContext<RuleListContext> context) {
        context.getObject().moveBack();
    }

    private <T> void handleRuleClick(ActionContext context, ClaimRule<T> rule) {
        Player player = context.getPlayer();
        InventoryClickEvent event = context.getEvent();
        RuleListContext listContext = this.getObject(context);

        this.controller.onRuleClick(player, rule, event, listContext);
    }

    private void handleCategoryClick(ObjectActionContext<RuleListContext> context, RuleCategory category) {
        Player player = context.getPlayer();
        RuleListContext data = context.getObject();
        RuleCategory next = data.category() == category ? null : category;

        context.getViewer().flushView(); // To recreate title with new category name.

        RuleListContext newContext = new RuleListContext(data.claim(), data.dirtyFlag(), next, data.onBack());

        this.show(player, newContext);
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
        RuleListContext data = this.getObject(context);
        RuleCategory category = data.category();
        Player player = context.getPlayer();
        Set<ClaimRule<?>> properties = new HashSet<>();
        if (category == null) {
            properties.addAll(this.rules.values());
        }
        else {
            properties.addAll(this.rules.values(category));
        }

        List<ClaimRule<?>> sortedProperties = properties.stream()
            .filter(property -> property.hasPermission(player))
            .sorted(Comparator.comparing(ClaimRule::getDisplayName))
            .toList();

        if (this.propertyPopulator != null) {
            this.propertyPopulator.populateTo(context, sortedProperties, items);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }
}
