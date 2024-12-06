package su.nightexpress.excellentclaims.flag.impl.list;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.type.ListMode;
import su.nightexpress.nightcore.util.bukkit.NightItem;

public class ListModeFlag extends EnumFlag<ListMode> {

    public ListModeFlag(@NotNull String id,
                        @NotNull FlagCategory category,
                        @NotNull ListMode defaultValue,
                        @NotNull NightItem icon,
                        @NotNull String... description) {
        super(id, category, ListMode.class, defaultValue, icon, description);
    }

    @Override
    @NotNull
    public String localize(@NotNull ListMode value) {
        return Lang.LIST_MODE.getLocalized(value);
    }
}
