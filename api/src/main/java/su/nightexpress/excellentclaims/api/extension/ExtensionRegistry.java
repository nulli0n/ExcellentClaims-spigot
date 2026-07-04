package su.nightexpress.excellentclaims.api.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExtensionRegistry<T extends Extension> {

    private final Map<Class<? extends T>, List<T>> extensions;

    public ExtensionRegistry() {
        this.extensions = new ConcurrentHashMap<>();
    }

    public void clear() {
        this.extensions.values().forEach(List::clear);
    }

    public void registerType(Class<? extends T> type) {
        this.extensions.putIfAbsent(type, new CopyOnWriteArrayList<>());
    }

    public void register(T extension) {
        boolean matched = false;

        for (var entry : this.extensions.entrySet()) {
            if (entry.getKey().isInstance(extension)) {
                entry.getValue().add(extension);
                matched = true;
            }
        }

        if (!matched) {
            throw new IllegalArgumentException(
                "Failed to register extension: " + extension.getClass().getSimpleName() +
                    " does not implement any registered Extension types."
            );
        }
    }

    public <E extends Extension> List<E> getExtensions(Class<E> type) {
        List<E> list = new ArrayList<>();

        this.extensions.getOrDefault(type, Collections.emptyList()).forEach(ext -> {
            list.add(type.cast(ext));
        });

        return list;
    }
}
