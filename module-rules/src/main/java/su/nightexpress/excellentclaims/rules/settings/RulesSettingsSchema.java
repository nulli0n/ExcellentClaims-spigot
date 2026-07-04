package su.nightexpress.excellentclaims.rules.settings;

import java.util.Set;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class RulesSettingsSchema {

    private static final String DEFAULT_PREFIX = TagWrappers.GRADIENT.with("#cb2d3e", "#ef473a")
        .and(TagWrappers.BOLD).wrap("PROTECTION") +
        " " + TagWrappers.DARK_GRAY.wrap("»") + " ";

    public static final ConfigProperty<String> PREFIX = ConfigProperty.of(ConfigCodecs.STRING,
        "General.Prefix",
        DEFAULT_PREFIX,
        ""
    );

    public static final ConfigProperty<Boolean> ALLOW_HIGH_FREQUENCY_RULES = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "General.Allow-High-Frequency-Rules",
        true,
        ".",
        "[Server restart required]"
    );

    public static final ConfigProperty<Boolean> RULES_BLOCK_GROW_RESET_AGE = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "Rules.BlockGrowEvent.Reset-Block-Age",
        true,
        "Whether to reset block's age value if claim rule denies block growing.",
        "This will reduce frequency of BlockGrowEvent event calls and grow rules checks.",
        "This also will prevent blocks from growing instantly when grow rule reset or set in allow mode.",
        "[Server restart required]"
    );

    public static final ConfigProperty<Set<String>> RULES_COMMAND_USE_DEFAULT_BLACKLIST = ConfigProperty.of(
        ConfigCodecs.STRING_SET,
        "Rules.Command-Use.Default-Blacklist",
        Set.of("sethome", "setwarp", "tpyes", "tpaccept"),
        ""
    );

    private RulesSettingsSchema() {
    }
}
