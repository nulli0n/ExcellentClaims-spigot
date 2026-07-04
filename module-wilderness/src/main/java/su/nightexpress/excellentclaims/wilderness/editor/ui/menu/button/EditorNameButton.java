package su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.wilderness.WildernessPlaceholders;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorService;
import su.nightexpress.excellentclaims.wilderness.editor.ui.WildernessEditorUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractMenuBase;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class EditorNameButton implements DefaultButtonExtension {

    private final WildernessEditorService      editor;
    private final WildernessEditorUIController controller;

    public EditorNameButton(WildernessEditorService editor, WildernessEditorUIController controller) {
        this.editor = editor;
        this.controller = controller;
    }

    @Override
    public void onLayoutDefine(AbstractMenuBase menu) {
        menu.addDefaultButton("wilderness_name", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BIRCH_HANGING_SIGN)
                    .setDisplayName(TagWrappers.GOLD.wrap(TagWrappers.BOLD.wrap("Name")))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Current: " + TagWrappers.GOLD.wrap(
                            WildernessPlaceholders.WILDERNESS_NAME)),
                        "",
                        TagWrappers.GOLD.wrap("→ " + TagWrappers.UNDERLINED.wrap("Click to change"))
                    ))
                    .hideAllComponents()
                )
                .displayModifier((context, item) -> {
                    item.replace(builder -> builder.with(context.getObject(WildernessRegion.class).placeholders()));
                })
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(11)
            .build()
        );
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        WildernessRegion claim = context.getObject(WildernessRegion.class);

        this.controller.onSetNameClick(player, claim, () -> context.getViewer().refresh());
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        WildernessRegion claim = context.getObject(WildernessRegion.class);

        return this.editor.canRename(player, claim).success();
    }
}
