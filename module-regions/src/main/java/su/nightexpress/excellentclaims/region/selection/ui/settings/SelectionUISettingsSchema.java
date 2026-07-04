package su.nightexpress.excellentclaims.region.selection.ui.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public final class SelectionUISettingsSchema {

    public static final ConfigProperty<Long> COMPONENT_REFRESH_RATE = ConfigProperty.of(
        ConfigCodecs.LONG,
        "General.Component-Refresh-Rate",
        10L,
        "Sets refresh rate (in game ticks) for the Selection UI components.",
        "[Asynchronous]"
    );

    public static final ConfigProperty<Boolean> COMPONENT_BAR_ENABLED = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "Components.BossBar",
        true,
        "Enables the BossBar UI component for Selection Mode."
    );

    public static final ConfigProperty<Boolean> COMPONENT_HIGHLIGHT_ENABLED = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "Components.Highlight",
        true,
        "Enables the Highlightning UI component for Selection Mode."
    );

    private SelectionUISettingsSchema() {
    }
}
