package su.nightexpress.excellentclaims.core.settings;

import java.nio.file.Path;
import java.util.function.Supplier;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.core.NightModule;
import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.excellentclaims.api.core.settings.SettingsController;

@NullMarked
public final class SettingsFactory {

    private SettingsFactory() {
    }

    /**
     * Creates, loads, registers, and attaches a settings object in one line.
     */
    public static <T extends ConfigurableSettings> T create(NightModule module,
                                                            DependencyContainer container,
                                                            Path filePath,
                                                            Supplier<T> constructor) {

        // Init Settings Object
        T settings = constructor.get();

        // Attach to the Universal Controller
        SettingsController controller = new SettingsController(filePath, settings);

        // Register to the dependency container so components can inject it
        @SuppressWarnings("unchecked") Class<T> type = (Class<T>) settings.getClass();
        container.register(type, settings);

        // Attach the controller to the parent module
        module.addComponent(controller);

        return settings;
    }
}