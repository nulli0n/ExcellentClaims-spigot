package su.nightexpress.excellentclaims.region.selection.ui.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class SelectionUISettings implements ConfigurableSettings {

    private long    componentRefreshRate;
    private boolean bossBarEnabled;
    private boolean highlightEnabled;

    @Override
    public void loadFrom(FileConfig config) {
        this.componentRefreshRate = config.getOrSet(SelectionUISettingsSchema.COMPONENT_REFRESH_RATE);
        this.bossBarEnabled = config.getOrSet(SelectionUISettingsSchema.COMPONENT_BAR_ENABLED);
        this.highlightEnabled = config.getOrSet(SelectionUISettingsSchema.COMPONENT_HIGHLIGHT_ENABLED);
    }

    public long getComponentRefreshRate() {
        return componentRefreshRate;
    }

    public boolean isBossBarEnabled() {
        return bossBarEnabled;
    }

    public boolean isHighlightEnabled() {
        return highlightEnabled;
    }
}
