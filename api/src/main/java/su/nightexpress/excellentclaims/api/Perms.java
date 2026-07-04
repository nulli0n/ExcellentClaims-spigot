package su.nightexpress.excellentclaims.api;

import org.bukkit.permissions.Permission;

import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class Perms {

    public static final PermissionNamespace ROOT = PermissionNamespace.root("excellentclaims");

    public static final PermissionNamespace COMMAND = ROOT.namespace("command");

    public static final Permission COMMAND_RELOAD = COMMAND.create("reload");

    private Perms() {
    }
}
