package su.nightexpress.excellentclaims.flag.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.flag.ClaimFlag;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.nightcore.config.FileConfig;

public class ParsedFlag<F extends ClaimFlag<T>, T> implements FlagValue {

    private final F flag;
    private final T value;

    public ParsedFlag(@NotNull F flag, @NotNull T value) {
        this.flag = flag;
        this.value = value;
    }

    @NotNull
    public F getFlag() {
        return flag;
    }

    @NotNull
    public T getValue() {
        return value;
    }

    @Override
    @NotNull
    public String getLocalized() {
        return this.flag.localize(this.value);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        this.flag.writeValue(config, path, this.value);
    }
}
