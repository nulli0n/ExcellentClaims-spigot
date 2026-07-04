package su.nightexpress.excellentclaims.region.selection.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class SelectionSettings implements ConfigurableSettings {

    private boolean uiEnabled;

    @Override
    public void loadFrom(FileConfig config) {
        this.uiEnabled = config.getOrSet(SelectionSettingsSchema.UI_ENABLED);
    }

    public boolean isUIEnabled() {
        return this.uiEnabled;
    }
}
