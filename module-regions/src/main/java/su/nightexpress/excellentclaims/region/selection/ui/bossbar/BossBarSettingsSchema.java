package su.nightexpress.excellentclaims.region.selection.ui.bossbar;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimsPlaceholders;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.util.Enums;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class BossBarSettingsSchema {

    private static final String DEFAULT_TITLE = (TagWrappers.WHITE.wrap("Region Selection") + " " +
        TagWrappers.GRAY.wrap("[%s/%s Blocks]") + " " +
        TagWrappers.DARK_GRAY.wrap("●") + " %s")
            .formatted(
                TagWrappers.WHITE.wrap(ClaimsPlaceholders.GENERIC_CURRENT),
                TagWrappers.YELLOW.wrap(ClaimsPlaceholders.GENERIC_MAX),
                ClaimsPlaceholders.GENERIC_STATE
            );

    public static final ConfigProperty<String> TITLE = ConfigProperty.of(
        ConfigCodecs.STRING,
        "Bar.Title",
        DEFAULT_TITLE,
        "Sets title for the Selection BossBar UI element.",
        "Placeholders:",
        "- %s - Number of currently selected blocks.".formatted(ClaimsPlaceholders.GENERIC_CURRENT),
        "- %s - Players' region blocks limit.".formatted(ClaimsPlaceholders.GENERIC_MAX),
        "- %s - Select pause state (ON/OFF).".formatted(ClaimsPlaceholders.GENERIC_STATE)
    );

    public static final ConfigProperty<NightBarColor> COLOR = ConfigProperty.of(
        ConfigCodecs.forEnum(NightBarColor.class),
        "Bar.Color",
        NightBarColor.BLUE,
        "Sets color for the Selection BossBar UI element.",
        "Available values: [%s]".formatted(Enums.inline(NightBarColor.class))
    );

    public static final ConfigProperty<NightBarOverlay> OVERLAY = ConfigProperty.of(
        ConfigCodecs.forEnum(NightBarOverlay.class),
        "Bar.Overlay",
        NightBarOverlay.PROGRESS,
        "Sets overlay for the Selection BossBar UI element.",
        "Available values: [%s]".formatted(Enums.inline(NightBarOverlay.class))
    );

    private BossBarSettingsSchema() {
    }
}
