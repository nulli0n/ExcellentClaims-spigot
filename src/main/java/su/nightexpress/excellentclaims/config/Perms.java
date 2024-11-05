package su.nightexpress.excellentclaims.config;

import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.nightcore.util.wrapper.UniPermission;

import java.util.function.Function;

public class Perms {

    public static final String PREFIX         = "excellentclaims.";
    public static final String PREFIX_COMMAND = PREFIX + "command.";
    public static final String PREFIX_BYPASS  = PREFIX + "bypass.";
    public static final String PREFIX_FLAG    = PREFIX + "flag.";

    public static final UniPermission PLUGIN  = new UniPermission(PREFIX + Placeholders.WILDCARD);
    public static final UniPermission COMMAND = new UniPermission(PREFIX_COMMAND + Placeholders.WILDCARD);
    public static final UniPermission BYPASS  = new UniPermission(PREFIX_BYPASS + Placeholders.WILDCARD);
    public static final UniPermission FLAG    = new UniPermission(PREFIX_FLAG + Placeholders.WILDCARD);

    public static final UniPermission CLAIMS_PRIORITY = new UniPermission(PREFIX + "claims.priority");

    public static final Function<FlagCategory, UniPermission> FLAG_TYPE = category -> new UniPermission(PREFIX + "flags." + category.name().toLowerCase());

    public static final UniPermission COMMAND_ADMIN_MODE = new UniPermission(PREFIX_COMMAND + "adminmode");

    public static final UniPermission COMMAND_WILDERNESS             = new UniPermission(PREFIX_COMMAND + "wilderness");
    public static final UniPermission COMMAND_WILDERNESS_FLAGS       = new UniPermission(PREFIX_COMMAND + "wilderness.flags");
    public static final UniPermission COMMAND_WILDERNESS_RENAME      = new UniPermission(PREFIX_COMMAND + "wilderness.rename");
    public static final UniPermission COMMAND_WILDERNESS_DESCRIPTION = new UniPermission(PREFIX_COMMAND + "wilderness.description");

    public static final UniPermission COMMAND_REGION             = new UniPermission(PREFIX_COMMAND + "region");
    public static final UniPermission COMMAND_REGION_WAND        = new UniPermission(PREFIX_COMMAND + "region.wand");
    public static final UniPermission COMMAND_REGION_CLAIM       = new UniPermission(PREFIX_COMMAND + "region.claim");
    public static final UniPermission COMMAND_REGION_REMOVE      = new UniPermission(PREFIX_COMMAND + "region.remove");
    public static final UniPermission COMMAND_REGION_FLAGS       = new UniPermission(PREFIX_COMMAND + "region.flags");
    public static final UniPermission COMMAND_REGION_MEMBERS     = new UniPermission(PREFIX_COMMAND + "region.members");
    public static final UniPermission COMMAND_REGION_SETTINGS    = new UniPermission(PREFIX_COMMAND + "region.settings");
    public static final UniPermission COMMAND_REGION_LIST        = new UniPermission(PREFIX_COMMAND + "region.list");
    public static final UniPermission COMMAND_REGION_LIST_OTHERS = new UniPermission(PREFIX_COMMAND + "region.list.others");
    public static final UniPermission COMMAND_REGION_LIST_ALL    = new UniPermission(PREFIX_COMMAND + "region.listall");
    public static final UniPermission COMMAND_REGION_SET_SPAWN   = new UniPermission(PREFIX_COMMAND + "region.setspawn");
    public static final UniPermission COMMAND_REGION_RENAME      = new UniPermission(PREFIX_COMMAND + "region.rename");
    public static final UniPermission COMMAND_REGION_DESCRIPTION = new UniPermission(PREFIX_COMMAND + "region.description");
    public static final UniPermission COMMAND_REGION_TRANSFER    = new UniPermission(PREFIX_COMMAND + "region.transfer");

    public static final UniPermission COMMAND_LAND             = new UniPermission(PREFIX_COMMAND + "land");
    public static final UniPermission COMMAND_LAND_CLAIM       = new UniPermission(PREFIX_COMMAND + "land.claim");
    public static final UniPermission COMMAND_LAND_UNCLAIM     = new UniPermission(PREFIX_COMMAND + "land.unclaim");
    public static final UniPermission COMMAND_LAND_FLAGS       = new UniPermission(PREFIX_COMMAND + "land.flags");
    public static final UniPermission COMMAND_LAND_MEMBERS     = new UniPermission(PREFIX_COMMAND + "land.members");
    public static final UniPermission COMMAND_LAND_SETTINGS    = new UniPermission(PREFIX_COMMAND + "land.settings");
    public static final UniPermission COMMAND_LAND_LIST        = new UniPermission(PREFIX_COMMAND + "land.list");
    public static final UniPermission COMMAND_LAND_LIST_OTHERS = new UniPermission(PREFIX_COMMAND + "land.list.others");
    public static final UniPermission COMMAND_LAND_LIST_ALL    = new UniPermission(PREFIX_COMMAND + "land.listall");
    public static final UniPermission COMMAND_LAND_MERGE       = new UniPermission(PREFIX_COMMAND + "land.merge");
    public static final UniPermission COMMAND_LAND_SEPARATE    = new UniPermission(PREFIX_COMMAND + "land.separate");
    public static final UniPermission COMMAND_LAND_SET_SPAWN   = new UniPermission(PREFIX_COMMAND + "land.setspawn");
    public static final UniPermission COMMAND_LAND_BOUNDS      = new UniPermission(PREFIX_COMMAND + "land.bounds");
    public static final UniPermission COMMAND_LAND_RENAME      = new UniPermission(PREFIX_COMMAND + "land.rename");
    public static final UniPermission COMMAND_LAND_DESCRIPTION = new UniPermission(PREFIX_COMMAND + "land.description");
    public static final UniPermission COMMAND_LAND_TRANSFER    = new UniPermission(PREFIX_COMMAND + "land.transfer");

    public static final UniPermission COMMAND_RELOAD = new UniPermission(PREFIX_COMMAND + "reload");

    public static final UniPermission BYPASS_CHUNK_CLAIM_OVERLAP  = new UniPermission(PREFIX_BYPASS + "chunk.claim.overlap");
    public static final UniPermission BYPASS_REGION_CLAIM_OVERLAP = new UniPermission(PREFIX_BYPASS + "region.claim.overlap");
    public static final UniPermission BYPASS_CHUNK_CLAIM_WORLD    = new UniPermission(PREFIX_BYPASS + "chunk.claim.world");
    public static final UniPermission BYPASS_REGION_CLAIM_WORLD   = new UniPermission(PREFIX_BYPASS + "region.claim.world");
    public static final UniPermission BYPASS_NAME_LENGTH          = new UniPermission(PREFIX_BYPASS + "name.length");
    public static final UniPermission BYPASS_DESCRIPTION_LENGTH   = new UniPermission(PREFIX_BYPASS + "description.length");

    static {
        PLUGIN.addChildren(
            COMMAND,
            BYPASS,
            FLAG,
            CLAIMS_PRIORITY
        );

        COMMAND.addChildren(
            COMMAND_ADMIN_MODE,

            COMMAND_WILDERNESS,
            COMMAND_WILDERNESS_DESCRIPTION,
            COMMAND_WILDERNESS_FLAGS,
            COMMAND_WILDERNESS_RENAME,

            COMMAND_REGION,
            COMMAND_REGION_WAND,
            COMMAND_REGION_CLAIM,
            COMMAND_REGION_REMOVE,
            COMMAND_REGION_FLAGS,
            COMMAND_REGION_MEMBERS,
            COMMAND_REGION_SETTINGS,
            COMMAND_REGION_LIST,
            COMMAND_REGION_LIST_OTHERS,
            COMMAND_REGION_LIST_ALL,
            COMMAND_REGION_SET_SPAWN,
            COMMAND_REGION_RENAME,
            COMMAND_REGION_DESCRIPTION,
            COMMAND_REGION_TRANSFER,

            COMMAND_LAND,
            COMMAND_LAND_CLAIM,
            COMMAND_LAND_UNCLAIM,
            COMMAND_LAND_FLAGS,
            COMMAND_LAND_MEMBERS,
            COMMAND_LAND_SETTINGS,
            COMMAND_LAND_LIST,
            COMMAND_LAND_LIST_OTHERS,
            COMMAND_LAND_LIST_ALL,
            COMMAND_LAND_MERGE,
            COMMAND_LAND_SEPARATE,
            COMMAND_LAND_SET_SPAWN,
            COMMAND_LAND_BOUNDS,
            COMMAND_LAND_RENAME,
            COMMAND_LAND_DESCRIPTION,
            COMMAND_LAND_TRANSFER,
            COMMAND_RELOAD
        );

        BYPASS.addChildren(
            BYPASS_CHUNK_CLAIM_OVERLAP,
            BYPASS_CHUNK_CLAIM_WORLD,
            BYPASS_REGION_CLAIM_OVERLAP,
            BYPASS_REGION_CLAIM_WORLD,
            BYPASS_NAME_LENGTH,
            BYPASS_DESCRIPTION_LENGTH
        );

        for (FlagCategory category : FlagCategory.values()) {
            PLUGIN.addChildren(FLAG_TYPE.apply(category));
        }
    }
}
