package su.nightexpress.excellentclaims.land;

import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;

public final class LandsPlaceholders {

    public static final String LAND_ID          = "%land_id%";
    public static final String LAND_NAME        = "%land_name%";
    public static final String LAND_RAW_NAME    = "%land_raw_name%";
    public static final String LAND_DESCRIPTION = "%land_description%";
    public static final String LAND_PRIORITY    = "%land_priority%";
    public static final String LAND_WORLD       = "%land_world%";
    public static final String LAND_SIZE        = "%land_size%";
    public static final String LAND_MEMBERS     = "%land_members%";

    public static final TypedPlaceholder<LandClaim> LAND = TypedPlaceholder.builder(LandClaim.class)
        .with(LAND_ID, land -> land.id().value())
        .with(LAND_NAME, LandClaim::getName)
        .with(LAND_RAW_NAME, LandClaim::getRawName)
        .with(LAND_DESCRIPTION, land -> land.getDescription() == null ? Lang.OTHER_NO_DESCRIPTION.text() : String
            .join("\n", land.getDescription()))
        .with(LAND_PRIORITY, land -> NumberUtil.format(land.getPriority()))
        .with(LAND_WORLD, land -> land.isEnabled() ? LangAssets.get(land.getWorld()) : land.getWorldKey()
            .asString())
        .with(LAND_SIZE, land -> String.valueOf(land.getChunkData().size()))
        .with(LAND_MEMBERS, land -> String.valueOf(land.getMembers().size()))
        .build();

    private LandsPlaceholders() {
    }
}
