package su.nightexpress.excellentclaims.rules.filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class FilteredSet<T> {

    private final Set<T> entries;

    private FilterMode mode;

    public FilteredSet(FilterMode mode, Set<T> entries) {
        this.entries = new HashSet<>(entries);
        this.mode = mode;
    }

    public static <T> FilteredSet<T> empty(FilterMode mode) {
        return new FilteredSet<>(mode, new HashSet<>());
    }

    public static <T> FilteredSet<T> valued(FilterMode mode, /*  ListType<T> type, */ List<T> values) {
        FilteredSet<T> list = empty(mode/* , type */);
        values.forEach(list::addEntry);
        return list;
    }

    public void clear() {
        this.entries.clear();
    }

    public int size() {
        return this.entries.size();
    }

    public boolean contains(T value) {
        return this.entries.contains(value);
    }

    public boolean addEntry(T entry) {
        return this.entries.add(entry);
    }

    public boolean removeEntry(T entry) {
        return this.entries.remove(entry);
    }

    public FilterMode getMode() {
        return this.mode;
    }

    public void setMode(FilterMode mode) {
        this.mode = mode;
    }

    public Set<T> getEntries() {
        return Set.copyOf(this.entries);
    }

    public int countEntries() {
        return this.entries.size();
    }

    public boolean isAllowed(T value) {
        return this.mode == FilterMode.WHITELIST == this.contains(value);
    }
}
