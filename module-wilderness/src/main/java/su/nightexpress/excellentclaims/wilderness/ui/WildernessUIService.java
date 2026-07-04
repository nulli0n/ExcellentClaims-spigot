package su.nightexpress.excellentclaims.wilderness.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.menu.ButtonExtension;
import su.nightexpress.excellentclaims.api.menu.ButtonExtensionHolder;
import su.nightexpress.excellentclaims.api.menu.ButtonExtensionRegistry;
import su.nightexpress.excellentclaims.api.menu.DefaultButtonExtension;
import su.nightexpress.excellentclaims.api.menu.DynamicButtonExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.ui.menu.WildernessMenus;

@NullMarked
public class WildernessUIService implements ButtonExtensionHolder {

    private final ClaimPermissionAPI permissions;

    private final ButtonExtensionRegistry extensions;

    @Nullable
    private WildernessMenus menus;

    public WildernessUIService(ClaimPermissionAPI permissions) {
        this.permissions = permissions;

        this.extensions = new ButtonExtensionRegistry();//new ArrayList<>();
        this.extensions.registerType(DynamicButtonExtension.class);
        this.extensions.registerType(DefaultButtonExtension.class);
    }

    public void registerMenus(WildernessMenus menus) {
        this.menus = menus;
    }

    @Override
    public void registerButton(ButtonExtension extension) {
        this.extensions.register(extension);
    }

    private WildernessMenus menus() {
        if (this.menus == null) throw new IllegalStateException("Menus are not initialized!");

        return this.menus;
    }

    public ActionResult canOpenClaimMenu(Player player, WildernessRegion claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.MANAGE_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult showClaimListMenu(Player player) {
        if (!this.menus().listMenu().show(player)) {
            return ActionResult.fail();
        }

        return ActionResult.ok();
    }

    public ActionResult showClaimMenu(Player player, WildernessRegion claim) {
        ActionResult check = this.canOpenClaimMenu(player, claim);
        if (!check.success()) return check;

        if (!this.menus().settingsMenu().show(player, claim)) {
            return ActionResult.fail();
        }

        return ActionResult.ok();
    }

    public ButtonExtensionRegistry getExtensions() {
        return this.extensions;
    }
}
