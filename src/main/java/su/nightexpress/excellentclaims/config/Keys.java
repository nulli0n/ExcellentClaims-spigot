package su.nightexpress.excellentclaims.config;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;

public class Keys {

    public static NamespacedKey itemType;
    //public static NamespacedKey wandItem;
    public static NamespacedKey mergeX;
    public static NamespacedKey mergeZ;

    public static void load(@NotNull ClaimPlugin plugin) {
        itemType = new NamespacedKey(plugin, "item_type");
        //wandItem = new NamespacedKey(plugin, "wand_item");

        mergeX = new NamespacedKey(plugin, "merge.x");
        mergeZ = new NamespacedKey(plugin, "merge.z");
    }

    public static void shutdown() {
        itemType = null;
        mergeX = null;
        mergeZ = null;
        //wandItem = null;
    }
}
