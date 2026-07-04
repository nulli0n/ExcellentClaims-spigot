package su.nightexpress.excellentclaims.wilderness;

import su.nightexpress.excellentclaims.core.Lang;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.TypedPlaceholder;

public final class WildernessPlaceholders {

    public static final String WILDERNESS_ID          = "%wilderness_id%";
    public static final String WILDERNESS_NAME        = "%wilderness_name%";
    public static final String WILDERNESS_RAW_NAME    = "%wilderness_raw_name%";
    public static final String WILDERNESS_DESCRIPTION = "%wilderness_description%";
    public static final String WILDERNESS_PRIORITY    = "%wilderness_priority%";
    public static final String WILDERNESS_WORLD       = "%wilderness_world%";

    public static final TypedPlaceholder<WildernessRegion> WILDERNESS = TypedPlaceholder.builder(WildernessRegion.class)
        .with(WILDERNESS_ID, land -> land.id().value())
        .with(WILDERNESS_NAME, WildernessRegion::getName)
        .with(WILDERNESS_RAW_NAME, WildernessRegion::getRawName)
        .with(WILDERNESS_DESCRIPTION, land -> land.getDescription() == null ? Lang.OTHER_NO_DESCRIPTION.text() : String
            .join("\n", land.getDescription()))
        .with(WILDERNESS_PRIORITY, land -> NumberUtil.format(land.getPriority()))
        .with(WILDERNESS_WORLD, land -> land.isEnabled() ? LangAssets.get(land.getWorld()) : land.getWorldKey()
            .asString())
        .build();

    private WildernessPlaceholders() {
    }
}
