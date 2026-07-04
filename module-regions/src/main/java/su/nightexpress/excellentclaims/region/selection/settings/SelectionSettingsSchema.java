package su.nightexpress.excellentclaims.region.selection.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public final class SelectionSettingsSchema {

    public static final ConfigProperty<Boolean> UI_ENABLED = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "Settings.UI.Enabled",
        true,
        "Whether the UI feature is enabled for Selection Mode.",
        "[Server restart required]"
    );

    private SelectionSettingsSchema() {
    }
}
