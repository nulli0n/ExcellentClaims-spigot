package su.nightexpress.excellentclaims.region;

import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;

public final class RegionsPlaceholders {

    public static final String REGION_ID          = "%region_id%";
    public static final String REGION_NAME        = "%region_name%";
    public static final String REGION_RAW_NAME    = "%region_raw_name%";
    public static final String REGION_DESCRIPTION = "%region_description%";
    public static final String REGION_PRIORITY    = "%region_priority%";
    public static final String REGION_WORLD       = "%region_world%";
    public static final String REGION_SIZE        = "%region_size%";
    public static final String REGION_MEMBERS     = "%region_members%";

    public static final TypedPlaceholder<RegionClaim> REGION = TypedPlaceholder.builder(RegionClaim.class)
        .with(REGION_ID, land -> land.id().value())
        .with(REGION_NAME, RegionClaim::getName)
        .with(REGION_RAW_NAME, RegionClaim::getRawName)
        .with(REGION_DESCRIPTION, land -> land.getDescription() == null ? Lang.OTHER_NO_DESCRIPTION.text() : String
            .join("\n", land.getDescription()))
        .with(REGION_PRIORITY, land -> NumberUtil.format(land.getPriority()))
        .with(REGION_WORLD, land -> land.isEnabled() ? LangAssets.get(land.getWorld()) : land.getWorldKey()
            .asString())
        .with(REGION_SIZE, land -> String.valueOf(land.getBoundingBox().getCuboid().getVolume()))
        .with(REGION_MEMBERS, land -> String.valueOf(land.getMembers().size()))
        .build();

    private RegionsPlaceholders() {
    }
}
