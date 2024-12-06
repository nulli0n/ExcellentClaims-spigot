package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.type.MaterialList;
import su.nightexpress.nightcore.ui.dialog.Dialog;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.Set;
import java.util.function.Predicate;

public class MaterialListFlag extends ListFlag<Material, MaterialList> {

    private final Predicate<Material> predicate;

    public MaterialListFlag(@NotNull String id,
                            @NotNull FlagCategory category,
                            @NotNull MaterialList defaultValue,
                            @NotNull Predicate<Material> predicate,
                            @NotNull NightItem icon,
                            @NotNull String... description
                            ) {
        super(id, category, MaterialList.class, defaultValue, icon, description);
        this.predicate = predicate;
    }

    @Override
    @Nullable
    public Material entryFromString(@NotNull String raw) {
        Material material = BukkitThing.getMaterial(raw);
        return material == null || !this.predicate.test(material) ? null : material;
    }

    @Override
    @NotNull
    public String entryToString(@NotNull Material entry) {
        return BukkitThing.toString(entry);
    }

    @Override
    @NotNull
    public MaterialList createList(@NotNull Set<Material> entries) {
        return new MaterialList(entries);
    }

    @Override
    protected Dialog.Builder onManagePrompt(@NotNull Dialog.Builder builder) {
        boolean isBlockRequired = this.predicate.test(Material.STONE);

        return builder.setPrompt((isBlockRequired ? Lang.FLAG_PROMPT_BLOCK_TYPE : Lang.FLAG_PROMPT_ITEM_TYPE));
    }
}
