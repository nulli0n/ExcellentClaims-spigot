package su.nightexpress.excellentclaims.api.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SimpleDependencies implements DependencyContainer {

    @Nullable
    private final DependencyContainer parent;

    // The local dependencies for this specific scope
    private final Map<Class<?>, Object> dependencies;

    // Creates a Root (Global) Container
    public SimpleDependencies() {
        this(null);
    }

    // Creates a Child (Module) Container
    public SimpleDependencies(@Nullable DependencyContainer parent) {
        this.parent = parent;
        this.dependencies = new HashMap<>();
    }

    public <T> void register(Class<T> type, @NonNull T instance) {
        this.dependencies.put(type, instance);
    }

    public <T> @NonNull T get(Class<T> type) {
        T service = this.getOrNull(type);
        if (service == null) {
            // Fail loudly if missing
            throw new IllegalStateException("Missing dependency: " + type.getSimpleName());
        }

        return service;
    }

    @Override
    public <T> @Nullable T getOrNull(Class<T> type) {
        // Check local scope first
        Object obj = this.dependencies.get(type);
        if (obj != null) {
            return type.cast(obj);
        }

        // Fall back to parent scope
        if (this.parent != null) {
            return this.parent.getOrNull(type);
        }

        return null;
    }

    @Override
    public <T> Optional<T> lookup(Class<T> type) {
        return Optional.ofNullable(this.getOrNull(type));
    }
}
