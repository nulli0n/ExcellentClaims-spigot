package su.nightexpress.excellentclaims.rules.settings;

import java.nio.file.Path;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.core.StandardMessageDispatcher;
import su.nightexpress.excellentclaims.core.settings.SettingsFactory;
import su.nightexpress.excellentclaims.rules.RulesModule;

public final class RulesSettingsConfiguration {

    private static final String SETTINGS_FILE = "settings.yml";

    private RulesSettingsConfiguration() {
    }

    public static void configure(RulesModule module, DependencyContainer container) {
        Path filePath = module.getModuleDir().resolve(SETTINGS_FILE);
        RulesSettings settings = SettingsFactory.create(module, container, filePath, RulesSettings::new);

        MessageDispatcher dispatcher = new StandardMessageDispatcher(settings);

        container.register(MessageDispatcher.class, dispatcher);
    }
}
