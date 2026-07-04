package su.nightexpress.excellentclaims.api.core;

import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface DependencyContainer {

    <T> void register(@NonNull Class<T> type, @NonNull T instance);

    <T> @NonNull T get(@NonNull Class<T> type);

    <T> @Nullable T getOrNull(@NonNull Class<T> type);

    <T> @NonNull Optional<T> lookup(@NonNull Class<T> type);
}
