package su.nightexpress.excellentclaims.flag.type;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.config.Lang;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class EntryList<T> {

    private final Set<T> entries;

    public EntryList(@NotNull Set<T> entries) {
        this.entries = entries;
    }

    @NotNull
    public Set<T> getEntries() {
        return this.entries;
    }

    @NotNull
    public abstract String localize(@NotNull T entry);

    public boolean isAllowed(@NotNull ListMode mode, @NotNull T entry) {
        if (mode == ListMode.ENABLED) return true;
        if (mode == ListMode.DISABLED) return false;

        boolean contains = this.entries.contains(entry);

        return (mode == ListMode.WHITELIST) == contains;
    }

    @NotNull
    public String getLocalizedString() {
        if (this.entries.isEmpty()) {
            return Lang.OTHER_EMPTY_LIST.getString();
        }
        return this.entries.stream().map(this::localize).map(Placeholders::goodEntry).collect(Collectors.joining("\n"));
    }
}
