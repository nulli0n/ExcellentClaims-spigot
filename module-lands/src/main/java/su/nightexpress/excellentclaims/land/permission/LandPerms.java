package su.nightexpress.excellentclaims.land.permission;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class LandPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("lands");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final Permission COMMAND_ROOT            = COMMAND.create("root");
    public static final Permission COMMAND_CLAIM           = COMMAND.create("claim");
    public static final Permission COMMAND_UNCLAIM         = COMMAND.create("unclaim");
    public static final Permission COMMAND_RULES           = COMMAND.create("rules");
    public static final Permission COMMAND_MEMBERS         = COMMAND.create("members");
    public static final Permission COMMAND_SETTINGS        = COMMAND.create("settings");
    public static final Permission COMMAND_LIST            = COMMAND.create("list");
    public static final Permission COMMAND_INSPECT         = COMMAND.create("inspect");
    public static final Permission COMMAND_MERGE           = COMMAND.create("merge");
    public static final Permission COMMAND_SPLIT           = COMMAND.create("split");
    public static final Permission COMMAND_SET_SPAWN       = COMMAND.create("setspawn");
    public static final Permission COMMAND_LAND_BORDERS    = COMMAND.create("borders");
    public static final Permission COMMAND_SET_NAME        = COMMAND.create("rename");
    public static final Permission COMMAND_SET_DESCRIPTION = COMMAND.create("description");
    public static final Permission COMMAND_SET_PRIORITY    = COMMAND.create("priority");
    public static final Permission COMMAND_TRANSFER        = COMMAND.create("transfer");

    public static final Permission BYPASS_CLAIMING_OVERLAP = BYPASS.create("claiming.overlap");
    public static final Permission BYPASS_CLAIMING_WORLD   = BYPASS.create("claiming.world");
    public static final Permission BYPASS_CLAIMING_COST    = BYPASS.create("claiming.cost");

    public static final Permission BYPASS_EDITOR_NAME_LENGTH        = BYPASS.create("editor.name.length");
    public static final Permission BYPASS_EDITOR_DESCRIPTION_LENGTH = BYPASS.create("editor.description.length");

    private LandPerms() {
    }
}
