package su.nightexpress.excellentclaims.region.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.excellentclaims.region.editor.ui.dialog.EditorDialogKeys;
import su.nightexpress.excellentclaims.region.editor.ui.menu.EditorMenus;
import su.nightexpress.excellentclaims.region.ui.RegionUIService;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;

public class RegionEditorUIService {

    private final DialogRegistry      dialogs;
    private final RegionUIService     coreUI; // used to know how to navigate back to claim menu
    private final RegionEditorService editor;

    @Nullable
    private EditorMenus menus;

    public RegionEditorUIService(DialogRegistry dialogs, RegionUIService coreUI, RegionEditorService editor) {
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

    public ActionResult openClaimMenu(Player player, RegionClaim claim) {
        return this.coreUI.showClaimMenu(player, claim);
    }

    public ActionResult openNameDialog(Player player, RegionClaim claim, Runnable callback) {
        ActionResult check = this.editor.canRename(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, EditorDialogKeys.NAME, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult openDescriptionDialog(Player player, RegionClaim claim, Runnable callback) {
        ActionResult check = this.editor.canChangeDescription(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, EditorDialogKeys.DESCRIPTION, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult openPriorityDialog(Player player, RegionClaim claim, Runnable callback) {
        ActionResult check = this.editor.canChangePriority(player, claim);
        if (!check.success()) return check;

        this.dialogs.show(player, EditorDialogKeys.PRIORITY, claim, callback);
        return ActionResult.ok();
    }

    public ActionResult openIconSelectionMenu(Player player, RegionClaim claim) {
        EditorMenus menus = this.checkMenus();

        ActionResult result = this.editor.canChangeIcon(player, claim);
        if (!result.success()) return result;

        menus.iconSelectionMenu().show(player, claim);
        return ActionResult.ok();
    }
}
