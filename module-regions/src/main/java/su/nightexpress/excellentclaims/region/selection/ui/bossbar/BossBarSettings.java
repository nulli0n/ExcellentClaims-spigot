package su.nightexpress.excellentclaims.region.selection.ui.bossbar;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.bridge.bossbar.NightBarColor;
import su.nightexpress.nightcore.bridge.bossbar.NightBarOverlay;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class BossBarSettings implements ConfigurableSettings {

    private String          title   = "";
    private NightBarColor   color   = NightBarColor.BLUE;
    private NightBarOverlay overlay = NightBarOverlay.PROGRESS;

    @Override
    public void loadFrom(FileConfig config) {
        this.title = config.getOrSet(BossBarSettingsSchema.TITLE);
        this.color = config.getOrSet(BossBarSettingsSchema.COLOR);
        this.overlay = config.getOrSet(BossBarSettingsSchema.OVERLAY);
    }

    public String getTitle() {
        return title;
    }

    public NightBarColor getColor() {
        return color;
    }

    public NightBarOverlay getOverlay() {
        return overlay;
    }
}
