package su.nightexpress.excellentclaims.land.settings;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.land.LandsModule;

@NullMarked
public final class LandSettingsConfiguration {

    private static final String SETTINGS_FILE = "lands.yml";

    private LandSettingsConfiguration() {
    }

    public static LandSettings configure(LandsModule module, DependencyContainer container) {
        Path file = module.getModuleDir().resolve(SETTINGS_FILE);
        return SettingsFactory.create(module, container, file, LandSettings::new);
    }
}
