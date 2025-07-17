package su.nightexpress.excellentclaims.flag.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.flag.FlagType;
import su.nightexpress.excellentclaims.api.flag.FlagValue;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.Optional;

public abstract class ClaimFlagType<T> implements FlagType<T> {

    protected final Class<T> clazz;

    public ClaimFlagType(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path, @NotNull T value) {
        config.set(path, value);
    }

    @Override
    @NotNull
    public Optional<T> unboxed(@NotNull FlagValue value) {
        Object result = value.getValue();
        if (this.clazz.isAssignableFrom(result.getClass())) {
            return Optional.of(this.clazz.cast(result));
        }

        return Optional.empty();
    }
}
