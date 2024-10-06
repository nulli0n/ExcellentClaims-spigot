package su.nightexpress.excellentclaims.flag.impl.list;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.flag.type.MaterialList;
import su.nightexpress.nightcore.dialog.Dialog;
import su.nightexpress.nightcore.util.BukkitThing;

import java.util.Set;
import java.util.function.Predicate;

public class MaterialListFlag extends ListFlag<Material, MaterialList> {

    private final Predicate<Material> predicate;

    public MaterialListFlag(@NotNull String id,
                            @NotNull FlagCategory category,
                            @NotNull MaterialList defaultValue,
                            @NotNull Predicate<Material> predicate,
                            @NotNull ItemStack icon,
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
    protected void onManagePrompt(@NotNull Player player, @NotNull Dialog dialog) {
        boolean isBlockRequired = this.predicate.test(Material.STONE);
        (isBlockRequired ? Lang.FLAG_PROMPT_BLOCK_TYPE : Lang.FLAG_PROMPT_ITEM_TYPE).getMessage().send(player);
    }
}
