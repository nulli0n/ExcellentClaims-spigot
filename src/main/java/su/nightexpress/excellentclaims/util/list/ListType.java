package su.nightexpress.excellentclaims.util.list;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ListType<T> {

    @NotNull Set<SmartEntry<T>> getAllValues();

    @NotNull Set<String> getAllValuesNames();

    @Nullable T parse(@NotNull String string);

    @NotNull SmartEntry<T> toEntry(@NotNull T value);

    @NotNull String getName(@NotNull T value);

    @NotNull String getNameLocalized(@NotNull T value);
}
