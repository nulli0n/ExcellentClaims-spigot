package su.nightexpress.excellentclaims.wilderness.permission;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class WildernessPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("wilderness");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final Permission COMMAND_REGION           = COMMAND.create("wilderness");
    public static final Permission COMMAND_WILDERNESS_RULES = COMMAND.create("wilderness.rules");
    public static final Permission COMMAND_SETTINGS         = COMMAND.create("wilderness.settings");
    public static final Permission COMMAND_LIST             = COMMAND.create("wilderness.list");
    public static final Permission COMMAND_SET_SPAWN        = COMMAND.create("wilderness.setspawn");
    public static final Permission COMMAND_SET_NAME         = COMMAND.create("wilderness.rename");
    public static final Permission COMMAND_SET_DESCRIPTION  = COMMAND.create("wilderness.description");
    public static final Permission COMMAND_SET_PRIORITY     = COMMAND.create("wilderness.priority");

    public static final Permission BYPASS_EDITOR_NAME_LENGTH        = BYPASS.create("editor.name.length");
    public static final Permission BYPASS_EDITOR_DESCRIPTION_LENGTH = BYPASS.create("editor.description.length");

    private WildernessPerms() {
    }
}
