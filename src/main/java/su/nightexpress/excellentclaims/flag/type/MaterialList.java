package su.nightexpress.excellentclaims.flag.type;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.LangAssets;

import java.util.HashSet;
import java.util.Set;

public class MaterialList extends EntryList<Material> {

    public MaterialList() {
        this(new HashSet<>());
    }

    public MaterialList(@NotNull Set<Material> entries) {
        super(entries);
    }

    @Override
    @NotNull
    public String localize(@NotNull Material entry) {
        return LangAssets.get(entry);
    }
}
