package su.nightexpress.excellentclaims.flag.type;

import org.bukkit.Keyed;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public abstract class KeyedList<E extends Keyed> extends EntryList<E> {

    public KeyedList() {
        this(new HashSet<>());
    }

    public KeyedList(@NotNull Set<E> entries) {
        super(entries);
    }
}
