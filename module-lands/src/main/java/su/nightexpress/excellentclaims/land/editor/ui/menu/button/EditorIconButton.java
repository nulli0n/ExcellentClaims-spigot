package su.nightexpress.excellentclaims.land.editor.ui.menu.button;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.menu.DynamicButtonExtension;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.editor.LandEditorService;
import su.nightexpress.excellentclaims.land.editor.ui.LandEditorUIController;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.settings.LandSettings;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@NullMarked
public class EditorIconButton implements DynamicButtonExtension {

    private final LandSettings           settings;
    private final LandEditorService      editor;
    private final LandEditorUIController controller;

    public EditorIconButton(LandSettings settings, LandEditorService editor, LandEditorUIController controller) {
        this.settings = settings;
        this.editor = editor;
        this.controller = controller;
    }

    // Use DynamicButton Extension to properly render current claim icon
    // and don't fill menu config with useless data.

    @Override
    public MenuItem getItem(ViewerContext context) {
        LandClaim claim = context.getObject(LandClaim.class);

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(claim.getIcon()
                    .localized(LandLang.UI_EDITOR_MENU_ICON_BUTTON)
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
        LandClaim claim = context.getObject(LandClaim.class);

        this.controller.onSetIconClick(player, claim);
    }

    private boolean checkPermission(ViewerContext context) {
        Player player = context.getPlayer();
        LandClaim claim = context.getObject(LandClaim.class);

        return this.editor.canChangeIcon(player, claim).success();
    }
}
