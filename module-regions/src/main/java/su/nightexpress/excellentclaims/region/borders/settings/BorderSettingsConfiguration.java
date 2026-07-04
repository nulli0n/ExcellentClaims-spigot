package su.nightexpress.excellentclaims.region.borders.settings;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimPlugin;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.settings.SettingsController;
import su.nightexpress.excellentclaims.region.borders.BordersModule;

@NullMarked
public final class BorderSettingsConfiguration {

    private static final String SETTINGS_FILE = "borders.yml";

    private BorderSettingsConfiguration() {
    }

    public static BorderSettings configure(BordersModule module, DependencyContainer container) {
        ClaimPlugin plugin = container.get(ClaimPlugin.class);

        // TODO Module dir
        Path settingsPath = plugin.dataPath().resolve(SETTINGS_FILE);

        BorderSettings settings = new BorderSettings();

        container.register(BorderSettings.class, settings);

        module.addComponent(new SettingsController(settingsPath, settings));

        return settings;
    }
}
