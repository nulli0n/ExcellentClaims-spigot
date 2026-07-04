package su.nightexpress.excellentclaims.region.ui;

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
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.ui.context.InspectContext;
import su.nightexpress.excellentclaims.region.ui.menu.RegionMenus;

@NullMarked
public class RegionUIService implements ButtonExtensionHolder {

    private final ClaimPermissionAPI permissions;

    private final ButtonExtensionRegistry extensions;

    @Nullable
    private RegionMenus menus;

    public RegionUIService(ClaimPermissionAPI permissions) {
        this.permissions = permissions;

        this.extensions = new ButtonExtensionRegistry();
        this.extensions.registerType(DynamicButtonExtension.class);
        this.extensions.registerType(DefaultButtonExtension.class);
    }

    public void registerMenus(RegionMenus menus) {
        this.menus = menus;
    }

    @Override
    public void registerButton(ButtonExtension extension) {
        this.extensions.register(extension);
    }

    private RegionMenus menus() {
        if (this.menus == null) throw new IllegalStateException("Menus are not initialized!");

        return this.menus;
    }

    public ActionResult canOpenClaimMenu(Player player, RegionClaim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.MANAGE_CLAIM)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult canInspectClaims(Player player) {
        if (!player.hasPermission(RegionPerms.COMMAND_INSPECT)) {
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

    public ActionResult showClaimInspectMenu(Player player, InspectContext context) {
        ActionResult check = this.canInspectClaims(player);
        if (!check.success()) return check;

        if (context.regions().isEmpty()) {
            return ActionResult.fail(RegionLang.REGION_INSPECT_EMPTY);
        }

        if (!this.menus().inspectMenu().show(player, context)) {
            return ActionResult.fail();
        }

        return ActionResult.ok();
    }

    public ActionResult showClaimMenu(Player player, RegionClaim claim) {
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
