package su.nightexpress.excellentclaims.land.permission;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class LandPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("lands");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final Permission COMMAND_LAND             = COMMAND.create("land");
    public static final Permission COMMAND_LAND_BORDERS     = COMMAND.create("land.borders");
    public static final Permission COMMAND_LAND_CLAIM       = COMMAND.create("land.claim");
    public static final Permission COMMAND_LAND_UNCLAIM     = COMMAND.create("land.unclaim");
    public static final Permission COMMAND_LAND_RULES       = COMMAND.create("land.rules");
    public static final Permission COMMAND_LAND_MEMBERS     = COMMAND.create("land.members");
    public static final Permission COMMAND_LAND_MENU        = COMMAND.create("land.menu");
    public static final Permission COMMAND_LAND_LIST        = COMMAND.create("land.list");
    public static final Permission COMMAND_LAND_INSPECT     = COMMAND.create("land.inspect");
    public static final Permission COMMAND_LAND_MERGE       = COMMAND.create("land.merge");
    public static final Permission COMMAND_LAND_SPLIT       = COMMAND.create("land.split");
    public static final Permission COMMAND_LAND_SET_HOME    = COMMAND.create("land.sethome");
    public static final Permission COMMAND_LAND_NAME        = COMMAND.create("land.name");
    public static final Permission COMMAND_LAND_DESCRIPTION = COMMAND.create("land.description");
    public static final Permission COMMAND_LAND_PRIORITY    = COMMAND.create("land.priority");
    public static final Permission COMMAND_LAND_TRANSFER    = COMMAND.create("land.transfer");

    public static final Permission BYPASS_CLAIMING_OVERLAP = BYPASS.create("claiming.overlap");
    public static final Permission BYPASS_CLAIMING_WORLD   = BYPASS.create("claiming.world");
    public static final Permission BYPASS_CLAIMING_COST    = BYPASS.create("claiming.cost");

    public static final Permission BYPASS_EDITOR_NAME_LENGTH        = BYPASS.create("editor.name.length");
    public static final Permission BYPASS_EDITOR_DESCRIPTION_LENGTH = BYPASS.create("editor.description.length");

    private LandPerms() {
    }
}
