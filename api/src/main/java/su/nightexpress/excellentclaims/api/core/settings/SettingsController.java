package su.nightexpress.excellentclaims.api.core.settings;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.LifecycleComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class SettingsController implements LifecycleComponent {

    private final Path                 path;
    private final ConfigurableSettings settings;

    public SettingsController(Path path, ConfigurableSettings settings) {
        this.path = path;
        this.settings = settings;

        this.loadSettings();
    }

    private void loadSettings() {
        FileConfig config = FileConfig.load(this.path);
        this.settings.loadFrom(config);
        config.saveChanges();
    }

    @Override
    public void reload() {
        this.loadSettings();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void start() {

    }
}
