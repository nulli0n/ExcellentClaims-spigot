package su.nightexpress.excellentclaims.flag.type;

import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.language.LangAssets;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class DamageTypeList extends KeyedList<DamageType> {

    public DamageTypeList() {
        super();
    }

    public DamageTypeList(@NotNull Set<DamageType> entries) {
        super(entries);
    }

    @Override
    @NotNull
    public String localize(@NotNull DamageType entry) {
        return LangAssets.get(entry);
    }
}
