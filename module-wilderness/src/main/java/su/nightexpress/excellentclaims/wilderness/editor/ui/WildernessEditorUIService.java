package su.nightexpress.excellentclaims.wilderness.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorService;
import su.nightexpress.excellentclaims.wilderness.editor.ui.dialog.EditorDialogKeys;
import su.nightexpress.excellentclaims.wilderness.editor.ui.menu.EditorMenus;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

public class WildernessEditorUIService {

    private final DialogRegistry          dialogs;
    private final WildernessUIService     coreUI; // used to know how to navigate back to claim menu
    private final WildernessEditorService editor;

    @Nullable
    private EditorMenus menus;

    public WildernessEditorUIService(DialogRegistry dialogs, WildernessUIService coreUI,
                                     WildernessEditorService editor) {
        this.dialogs = dialogs;
        this.coreUI = coreUI;
        this.editor = editor;
    }

    public void registerMenus(EditorMenus menus) {
        this.menus = menus;
    }

    private EditorMenus checkMenus() {
        if (this.menus == null) throw new IllegalStateException("Menus are not initialized!");

        return this.menus;
    }

    public ActionResult openClaimMenu(Player player, WildernessRegion claim) {
        return this.coreUI.showClaimMenu(player, claim);
    }

    public ActionResult openNameDialog(Player player, WildernessRegion claim, Runnable callback) {
        ActionResult check = this.editor.canRename(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, EditorDialogKeys.NAME, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult openDescriptionDialog(Player player, WildernessRegion claim, Runnable callback) {
        ActionResult check = this.editor.canChangeDescription(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, EditorDialogKeys.DESCRIPTION, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult openPriorityDialog(Player player, WildernessRegion claim, Runnable callback) {
        ActionResult check = this.editor.canChangePriority(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, EditorDialogKeys.PRIORITY, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult openIconSelectionMenu(Player player, WildernessRegion claim) {
        EditorMenus menus = this.checkMenus();

        ActionResult result = this.editor.canChangeIcon(player, claim);
        if (!result.success()) return result;

        menus.iconSelectionMenu().show(player, claim);
        return ActionResult.ok();
    }
}
