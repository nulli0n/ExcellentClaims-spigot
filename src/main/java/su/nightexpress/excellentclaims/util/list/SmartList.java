package su.nightexpress.excellentclaims.util.list;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.config.Writeable;

import java.util.*;

public class SmartList<T> implements Writeable {

    private final Map<String, T> entryMap;
    private final ListType<T>    type;

    private ListMode mode;

    public SmartList(@NotNull ListMode mode, @NotNull Map<String, T> entryMap, @NotNull ListType<T> type) {
        this.entryMap = entryMap;
        this.type = type;
        this.mode = mode;
    }

    @NotNull
    public static <T> SmartList<T> empty(@NotNull ListMode mode, @NotNull ListType<T> type) {
        return new SmartList<>(mode, new HashMap<>(), type);
    }

    @NotNull
    public static <T> SmartList<T> rawValues(@NotNull ListMode mode, @NotNull ListType<T> type, @NotNull List<String> values) {
        return valued(mode, type, values.stream().map(type::parse).filter(Objects::nonNull).toList());
    }

    @NotNull
    public static <T> SmartList<T> valued(@NotNull ListMode mode, @NotNull ListType<T> type, @NotNull List<T> values) {
        SmartList<T> list = empty(mode, type);
        values.forEach(value -> list.addEntry(type.toEntry(value)));
        return list;
    }

//    @NotNull
//    public static <T> SmartList<T> read(@NotNull FileConfig config, @NotNull String path, @NotNull ListType<T> type) {
//        ListMode mode = ConfigValue.create(path + ".Mode", ListMode.class, ListMode.BLACKLIST).read(config);
//        Map<String, T> entries = new HashMap<>();
//
//        ConfigValue.create(path + ".Entries", new HashSet<>()).read(config).stream()
//            .map(type::parse)
//            .filter(Objects::nonNull)
//            .map(type::toEntry)
//            .forEach(entry -> entries.put(entry.getName(), entry.getBackend()));
//            //.collect(Collectors.toCollection(HashSet::new));
//
//        return new SmartList<>(mode, entries, type);
//    }

    public void load(@NotNull FileConfig config, @NotNull String path) {
        this.clear();

        this.setMode(ConfigValue.create(path + ".Mode", ListMode.class, ListMode.BLACKLIST).read(config));

        ConfigValue.create(path + ".Entries", new HashSet<>()).read(config).forEach(this::addEntry);
    }

    @Override
    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Mode", this.mode.name());
        config.set(path + ".Entries", this.entryMap.keySet());
    }

    public void clear() {
        this.entryMap.clear();
    }

    public boolean containsEntry(@NotNull String name) {
        return this.entryMap.containsKey(name.toLowerCase());
    }

    public boolean removeEntry(@NotNull String name) {
        return this.entryMap.remove(name.toLowerCase()) != null;
    }

    public boolean addEntry(@NotNull String name) {
        T value = this.type.parse(name);
        if (value == null) return false;

        SmartEntry<T> entry = this.type.toEntry(value);
        this.addEntry(entry);
        return true;
    }

    public void addEntry(@NotNull SmartEntry<T> entry) {
        this.entryMap.put(entry.getName(), entry.getBackend());
    }

    @NotNull
    public ListType<T> getType() {
        return this.type;
    }

    @NotNull
    public ListMode getMode() {
        return this.mode;
    }

    public void setMode(@NotNull ListMode mode) {
        this.mode = mode;
    }

    @NotNull
    public Map<String, T> getEntryMap() {
        return this.entryMap;
    }

    @NotNull
    public Set<T> getEntries() {
        return new HashSet<>(this.entryMap.values());
    }

    public int countEntries() {
        return this.entryMap.size();
    }

    public boolean isAllowed(@NotNull T value) {
        String name = this.type.getName(value);

        boolean contains = this.entryMap.containsKey(name);

        return (this.mode == ListMode.WHITELIST) == contains;
    }
}
