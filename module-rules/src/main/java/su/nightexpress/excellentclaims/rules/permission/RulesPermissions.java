package su.nightexpress.excellentclaims.rules.permission;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class RulesPermissions {

    public static final PermissionNamespace ROOT = Perms.ROOT.namespace("rules");
    public static final PermissionNamespace RULE = ROOT.namespace("rule");

    public static Permission forRule(String id) {
        return RULE.create(id);
    }

    private RulesPermissions() {
    }
}
