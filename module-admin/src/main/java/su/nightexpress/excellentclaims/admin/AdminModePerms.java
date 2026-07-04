package su.nightexpress.excellentclaims.admin;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class AdminModePerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("admin");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission COMMAND_ADMIN_MODE = COMMAND.create("adminmode");

    private AdminModePerms() {
    }
}
