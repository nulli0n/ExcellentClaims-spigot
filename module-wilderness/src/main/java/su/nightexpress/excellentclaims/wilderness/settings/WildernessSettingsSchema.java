package su.nightexpress.excellentclaims.wilderness.settings;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class WildernessSettingsSchema {

    private static final String DEFAULT_PREFIX = TagWrappers.GRADIENT.with("#4caf50", "#8bc34a")
        .and(TagWrappers.BOLD)
        .wrap("WILDERNESS") + TagWrappers.DARK_GRAY.wrap(" » ");

    public static final ConfigProperty<String> PREFIX = ConfigProperty.of(ConfigCodecs.STRING,
        "Core.Prefix",
        DEFAULT_PREFIX,
        "Prefix for all Wilderness-related chat messages."
    );

    public static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(ConfigCodecs.STRING_ARRAY,
        "Core.Command-Aliases",
        new String[]{"wilderness", "wild"},
        "Command aliases for the Wilderness module."
    );

    public static final ConfigProperty<NightItem> WILDERNESS_DEFAULT_ICON = ConfigProperty.of(ConfigCodecs.NIGHT_ITEM,
        "Wilderness.Default-Icon",
        NightItem.fromType(Material.GRASS_BLOCK),
        "Default icon for new wilderness regions."
    );

    public static final ConfigProperty<String> WILDERNESS_DEFAULT_NAME = ConfigProperty.of(
        ConfigCodecs.STRING,
        "Wilderness.Default-Name",
        TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Wilderness"),
        ""
    );

    public static final ConfigProperty<Integer> WILDERNESS_DEFAULT_PRIORITY = ConfigProperty.of(ConfigCodecs.INT,
        "Wilderness.Default-Priority",
        0,
        "Default priority value for new wilderness regions.",
        "[Default is 0]"
    );

    public static final ConfigProperty<Integer> WILDERNESS_NAME_MAX_LENGTH = ConfigProperty.of(ConfigCodecs.INT,
        "Wilderness.Name.Max-Length",
        24,
        "Sets max. allowed length for wilderness region names.",
        "Color & decoration tags do not count."
    );

    public static final ConfigProperty<Integer> WILDERNESS_DESCRIPTION_MAX_LENGTH = ConfigProperty.of(ConfigCodecs.INT,
        "Wilderness.Description.Max-Length",
        48,
        "Sets max. allowed length for wilderness region descriptions.",
        "Color & decoration tags do not count."
    );

    public static final ConfigProperty<Integer> WILDERNESS_PRIORITY_MIN_VALUE = ConfigProperty.of(ConfigCodecs.INT,
        "Wilderness.Priority.Min-Value",
        0,
        "Set min. allowed priority value for wilderness regions."
    );

    public static final ConfigProperty<Integer> WILDERNESS_PRIORITY_MAX_VALUE = ConfigProperty.of(ConfigCodecs.INT,
        "Wilderness.Priority.Max-Value",
        10,
        "Set max. allowed priority value for wilderness regions."
    );

    public static final ConfigProperty<Integer> UI_SETTINGS_ICON_SLOT = ConfigProperty.of(ConfigCodecs.INT,
        "UI.Settings.Icon-Slot",
        13,
        "Sets slot for the 'Icon' button in Wilderness GUI."
    );

    private WildernessSettingsSchema() {
    }
}
