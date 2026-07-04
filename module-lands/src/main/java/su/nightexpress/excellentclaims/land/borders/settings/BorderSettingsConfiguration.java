package su.nightexpress.excellentclaims.land.borders.settings;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.land.LandsModule;
import su.nightexpress.excellentclaims.land.borders.BordersModule;

@NullMarked
public final class BorderSettingsConfiguration {

    private static final String SETTINGS_FILE = "borders.yml";

    private BorderSettingsConfiguration() {
    }

    public static BorderSettings configure(BordersModule module, DependencyContainer container) {
        LandsModule parent = container.get(LandsModule.class);
        Path filePath = parent.getModuleDir().resolve(SETTINGS_FILE);

        return SettingsFactory.create(module, container, filePath, BorderSettings::new);
    }
}
