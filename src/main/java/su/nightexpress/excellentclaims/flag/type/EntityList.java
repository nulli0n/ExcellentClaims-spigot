package su.nightexpress.excellentclaims.flag.type;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.LangAssets;

import java.util.HashSet;
import java.util.Set;

public class EntityList extends EntryList<EntityType> {

    public EntityList() {
        this(new HashSet<>());
    }

    public EntityList(@NotNull Set<EntityType> entries) {
        super(entries);
    }

    @Override
    @NotNull
    public String localize(@NotNull EntityType entry) {
        return LangAssets.get(entry);
    }
}
