package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.Registry;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.type.DamageTypeList;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.util.BukkitThing;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class DamageTypeListFlag extends ListFlag<DamageType, DamageTypeList> {

    public DamageTypeListFlag(@NotNull String id,
                              @NotNull FlagCategory category,
                              @NotNull DamageTypeList defaultValue,
                              @NotNull ItemStack icon,
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
    protected void onManagePrompt(@NotNull Player player, @NotNull Dialog dialog) {
        Set<String> types = BukkitThing.allFromRegistry(Registry.DAMAGE_TYPE).stream()
            .map(BukkitThing::toString)
            .collect(Collectors.toSet());
        dialog.setSuggestions(types, true);

        Lang.FLAG_PROMPT_DAMAGE_TYPE.getMessage().send(player);
    }
}
