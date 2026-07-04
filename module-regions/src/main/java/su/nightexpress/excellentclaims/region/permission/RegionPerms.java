package su.nightexpress.excellentclaims.region.permission;

import org.bukkit.permissions.Permission;

import su.nightexpress.excellentclaims.api.Perms;
import su.nightexpress.nightcore.bridge.permission.PermissionNamespace;

public final class RegionPerms {

    public static final PermissionNamespace ROOT    = Perms.ROOT.namespace("regions");
    public static final PermissionNamespace COMMAND = ROOT.namespace("command");
    public static final PermissionNamespace BYPASS  = ROOT.namespace("bypass");

    public static final Permission COMMAND_REGION          = COMMAND.create("region");
    public static final Permission COMMAND_REGION_WAND     = COMMAND.create("region.wand");
    public static final Permission COMMAND_CLAIM           = COMMAND.create("region.claim");
    public static final Permission COMMAND_REGION_REMOVE   = COMMAND.create("region.remove");
    public static final Permission COMMAND_RULES           = COMMAND.create("region.rules");
    public static final Permission COMMAND_MEMBERS         = COMMAND.create("region.members");
    public static final Permission COMMAND_SETTINGS        = COMMAND.create("region.settings");
    public static final Permission COMMAND_LIST            = COMMAND.create("region.list");
    public static final Permission COMMAND_INSPECT         = COMMAND.create("region.inspect");
    public static final Permission COMMAND_SET_SPAWN       = COMMAND.create("region.setspawn");
    public static final Permission COMMAND_BORDERS         = COMMAND.create("region.borders");
    public static final Permission COMMAND_SET_NAME        = COMMAND.create("region.rename");
    public static final Permission COMMAND_SET_DESCRIPTION = COMMAND.create("region.description");
    public static final Permission COMMAND_SET_PRIORITY    = COMMAND.create("region.priority");
    public static final Permission COMMAND_TRANSFER        = COMMAND.create("region.transfer");

    public static final Permission BYPASS_CLAIMING_OVERLAP = BYPASS.create("claiming.overlap");
    public static final Permission BYPASS_CLAIMING_WORLD   = BYPASS.create("claiming.world");
    public static final Permission BYPASS_CLAIMING_COST    = BYPASS.create("claiming.cost");

    public static final Permission BYPASS_EDITOR_NAME_LENGTH        = BYPASS.create("editor.name.length");
    public static final Permission BYPASS_EDITOR_DESCRIPTION_LENGTH = BYPASS.create("editor.description.length");

    private RegionPerms() {
    }
}
