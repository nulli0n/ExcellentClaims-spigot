package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.type.EntityList;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.util.BukkitThing;

import java.util.Set;
import java.util.stream.Collectors;

public class EntityListFlag extends ListFlag<EntityType, EntityList> {

    public EntityListFlag(@NotNull String id,
                          @NotNull FlagCategory category,
                          @NotNull EntityList defaultValue,
                          @NotNull ItemStack icon,
                          @NotNull String... description
                          ) {
        super(id, category, EntityList.class, defaultValue, icon, description);
    }


    @Override
    @Nullable
    public EntityType entryFromString(@NotNull String raw) {
        return BukkitThing.getEntityType(raw);
    }

    @Override
    @NotNull
    public String entryToString(@NotNull EntityType entry) {
        return BukkitThing.toString(entry);
    }

    @Override
    @NotNull
    public EntityList createList(@NotNull Set<EntityType> entries) {
        return new EntityList(entries);
    }

    @Override
    public void onManagePrompt(@NotNull Player player, @NotNull Dialog dialog) {
        Lang.FLAG_PROMPT_ENTITY_TYPE.getMessage().send(player);

        // TODO nightcore update localized <-> id
        Set<String> types = BukkitThing.allFromRegistry(Registry.ENTITY_TYPE).stream()
            .filter(EntityType::isSpawnable)
            .map(BukkitThing::toString)
            .collect(Collectors.toSet());

        dialog.setSuggestions(types, true);
    }
}