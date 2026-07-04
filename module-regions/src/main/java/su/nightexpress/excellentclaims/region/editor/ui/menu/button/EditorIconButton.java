package su.nightexpress.excellentclaims.region.editor.ui.menu.button;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DynamicButtonExtension;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.excellentclaims.region.editor.ui.RegionEditorUIController;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.settings.RegionSettings;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@NullMarked
public class EditorIconButton implements DynamicButtonExtension {

    private final RegionSettings           settings;
    private final RegionEditorService      editor;
    private final RegionEditorUIController controller;

    public EditorIconButton(RegionSettings settings, RegionEditorService editor, RegionEditorUIController controller) {
        this.settings = settings;
        this.editor = editor;
        this.controller = controller;
    }

    // Use DynamicButton Extension to properly render current claim icon
    // and don't fill menu config with useless data.

    @Override
    public MenuItem getItem(ViewerContext context) {
        RegionClaim claim = context.getObject(RegionClaim.class);

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(claim.getIcon()
                    .localized(RegionLang.UI_EDITOR_MENU_ICON_BUTTON)
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
        RegionClaim claim = context.getObject(RegionClaim.class);

        this.controller.onSetIconClick(player, claim);
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        RegionClaim claim = context.getObject(RegionClaim.class);

        return this.editor.canChangeIcon(player, claim).success();
    }
}
