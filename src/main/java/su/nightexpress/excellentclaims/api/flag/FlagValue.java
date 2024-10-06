package su.nightexpress.excellentclaims.api.flag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;

public interface FlagValue {

    @NotNull Flag getFlag();

    @NotNull Object getValue();

    @NotNull String getLocalized();

    void write(@NotNull FileConfig config, @NotNull String path);
}
