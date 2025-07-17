package su.nightexpress.excellentclaims.flag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.flag.FlagType;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.nightcore.config.FileConfig;

public class ParsedFlag<T> implements FlagValue {

    private final FlagType<T> type;
    private final T value;

    public ParsedFlag(@NotNull FlagType<T> type, @NotNull T value) {
        this.type = type;
        this.value = value;
    }

    @NotNull
    @Override
    public FlagType<T> getType() {
        return this.type;
    }

    @NotNull
    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        this.type.write(config, path, this.value);
    }
}
