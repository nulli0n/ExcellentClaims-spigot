package su.nightexpress.excellentclaims.api.core;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ConfigurableModule extends NightModule {

    Path getModuleDir();
}
