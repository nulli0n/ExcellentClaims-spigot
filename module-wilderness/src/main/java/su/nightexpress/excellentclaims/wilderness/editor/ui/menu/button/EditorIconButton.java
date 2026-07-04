package su.nightexpress.excellentclaims.wilderness.editor.ui.menu.button;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DynamicButtonExtension;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorService;
import su.nightexpress.excellentclaims.wilderness.editor.ui.WildernessEditorUIController;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.excellentclaims.wilderness.settings.WildernessSettings;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@NullMarked
public class EditorIconButton implements DynamicButtonExtension {

    private final WildernessSettings           settings;
    private final WildernessEditorService      editor;
    private final WildernessEditorUIController controller;

    public EditorIconButton(WildernessSettings settings, WildernessEditorService editor,
                            WildernessEditorUIController controller) {
        this.settings = settings;
        this.editor = editor;
        this.controller = controller;
    }

    // Use DynamicButton Extension to properly render current claim icon
    // and don't fill menu config with useless data.

    @Override
    public MenuItem getItem(ViewerContext context) {
        WildernessRegion claim = context.getObject(WildernessRegion.class);

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(claim.getIcon()
                    .localized(WildernessLang.UI_EDITOR_MENU_ICON_BUTTON)
                    .hideAllComponents()
                )
                .condition(this::checkPermission)
                .action(this::onClick)
                .build()
            )
            .slots(this.settings.getUISettingsIconSlot())
            .build();
    }

    private void onClick(ActionContext context) {
        Player player = context.getPlayer();
        WildernessRegion claim = context.getObject(WildernessRegion.class);

        this.controller.onSetIconClick(player, claim);
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        WildernessRegion claim = context.getObject(WildernessRegion.class);

        return this.editor.canChangeIcon(player, claim).success();
    }
}
