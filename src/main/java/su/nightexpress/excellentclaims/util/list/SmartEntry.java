package su.nightexpress.excellentclaims.util.list;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class SmartEntry<T> /*implements ListEntry*/ {

    private final T backend;
    private final NightItem icon;
    private final String name;
    private final String localizedName;

    public SmartEntry(@NotNull T backend, @NotNull NightItem icon, @NotNull String name, @NotNull String localizedName) {
        this.backend = backend;
        this.icon = icon;
        this.name = name;
        this.localizedName = localizedName;
    }

    @NotNull
    public T getBackend() {
        return this.backend;
    }

    @NotNull
    public NightItem getIcon() {
        return this.icon;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getLocalizedName() {
        return this.localizedName;
    }
}
