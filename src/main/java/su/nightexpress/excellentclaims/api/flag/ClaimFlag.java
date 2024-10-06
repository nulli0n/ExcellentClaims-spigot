package su.nightexpress.excellentclaims.api.flag;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;

public interface ClaimFlag<T> extends Flag {

    //@NotNull T fromString(@NotNull String string);

    //@NotNull String toString(@NotNull T value);

    @NotNull String localize(@NotNull T value);

    @NotNull T readValue(@NotNull FileConfig config, @NotNull String path);

    void writeValue(@NotNull FileConfig config, @NotNull String path, @NotNull T value);

    @NotNull
    default FlagValue asDefaultValue() {
        return this.asValue(this.getDefaultValue());
    }

    @NotNull FlagValue asValue(@NotNull T value);

    @NotNull FlagValue parse(@NotNull FileConfig config, @NotNull String path);

    @NotNull Class<T> getValueType();

    @NotNull T getDefaultValue();
}
