package su.nightexpress.excellentclaims.rules.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleListContext;
import su.nightexpress.excellentclaims.rules.ui.menu.RuleMenus;

@NullMarked
public class RuleUIService {

    private final ClaimPermissionAPI permissions;

    @Nullable
    private RuleMenus menus;

    public RuleUIService(ClaimPermissionAPI permissions) {
        this.permissions = permissions;
    }

    public void registerMenus(RuleMenus menus) {
        this.menus = menus;
    }

    private RuleMenus menus() {
        if (this.menus == null) throw new IllegalStateException("Menus are not initialized!");

        return this.menus;
    }

    public ActionResult canOpenRules(Player player, Claim claim) {
        if (!this.permissions.hasPermission(player, claim, ClaimPermission.MANAGE_RULES)) {
            return ActionResult.fail(Lang.ERROR_NO_CLAIM_PERMISSION);
        }

        return ActionResult.ok();
    }

    public ActionResult openRules(Player player, RuleListContext context) {
        Claim claim = context.claim();
        ActionResult check = this.canOpenRules(player, claim);
        if (!check.success()) return check;

        if (!this.menus().rulesMenu().show(player, context)) {
            return ActionResult.fail();
        }

        return ActionResult.ok();
    }
}
