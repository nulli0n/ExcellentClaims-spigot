package su.nightexpress.excellentclaims.rules.filter.codec;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.filter.StringAdapter;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.Lists;

@NullMarked
public class FilterTypeCodec<T> implements ConfigCodec<FilteredSet<T>> {

    private final StringAdapter<T> adapter;

    public FilterTypeCodec(StringAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public FilteredSet<T> read(FileConfig config, String path) throws CodecReadException {
        FilterMode mode = config.getEnum(path + ".Mode", FilterMode.class, FilterMode.BLACKLIST);

        Set<T> entries = new HashSet<>();
        Set<String> rawEntries = config.getOrSet(path + ".Entries", ConfigCodecs.STRING_SET, Set.of());

        rawEntries.stream()
            .map(this.adapter::deserialize)
            .filter(Objects::nonNull)
            .forEach(entries::add);

        return new FilteredSet<>(mode, entries);
    }

    @Override
    public void write(FileConfig config, String path, FilteredSet<T> value) {
        config.set(path + ".Mode", value.getMode().name());
        config.set(path + ".Entries", Lists.modify(value.getEntries(), this.adapter::serialize));
    }
}
