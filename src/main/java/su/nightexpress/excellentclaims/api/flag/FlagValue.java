package su.nightexpress.excellentclaims.api.flag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;

public interface FlagValue {

    @NotNull Object getValue();

    @NotNull FlagType<?> getType();

    void write(@NotNull FileConfig config, @NotNull String path);
}
