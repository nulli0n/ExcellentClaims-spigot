package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.Registry;
import org.bukkit.damage.DamageType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.type.DamageTypeList;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.List;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public class DamageTypeListFlag extends ListFlag<DamageType, DamageTypeList> {

    public DamageTypeListFlag(@NotNull String id,
                              @NotNull FlagCategory category,
                              @NotNull DamageTypeList defaultValue,
                              @NotNull NightItem icon,
                              @NotNull String... description) {
        super(id, category, DamageTypeList.class, defaultValue, icon, description);
    }

    @Override
    @Nullable
    public DamageType entryFromString(@NotNull String raw) {
        return BukkitThing.fromRegistry(Registry.DAMAGE_TYPE, raw);
    }

    @Override
    @NotNull
    public String entryToString(@NotNull DamageType entry) {
        return BukkitThing.toString(entry);
    }

    @Override
    @NotNull
    public DamageTypeList createList(@NotNull Set<DamageType> entries) {
        return new DamageTypeList(entries);
    }

    @Override
    protected Dialog.Builder onManagePrompt(@NotNull Dialog.Builder builder) {
        List<String> types = BukkitThing.allFromRegistry(Registry.DAMAGE_TYPE).stream()
            .map(BukkitThing::toString)
            .toList();

        return builder.setPrompt(Lang.FLAG_PROMPT_DAMAGE_TYPE).setSuggestions(types, true);
    }
}
