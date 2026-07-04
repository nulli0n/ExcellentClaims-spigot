package su.nightexpress.excellentclaims.api.core.settings;

import org.jspecify.annotations.NonNull;

import su.nightexpress.nightcore.config.FileConfig;

public interface ConfigurableSettings {

    void loadFrom(@NonNull FileConfig config);
}
