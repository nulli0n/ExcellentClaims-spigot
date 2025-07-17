package su.nightexpress.excellentclaims.util;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;

public class BlockTypes {

    public static boolean isPressurePlate(@NotNull Material material) {
        return Tag.PRESSURE_PLATES.isTagged(material);
    }

    public static boolean isButtonLever(@NotNull Material material) {
        return Tag.BUTTONS.isTagged(material) || material == Material.LEVER;
        //return material.createBlockData() instanceof Switch;
    }

    public static boolean isDoor(@NotNull Material material) {
        return Tag.DOORS.isTagged(material) || Tag.TRAPDOORS.isTagged(material) || Tag.FENCE_GATES.isTagged(material);
    }

    public static boolean isTramplable(@NotNull Material material) {
        return material == Material.TURTLE_EGG;
    }

    public static boolean isTripwire(@NotNull Material material) {
        return material == Material.TRIPWIRE;
    }

    public static boolean isSign(@NotNull Material material) {
        return Tag.SIGNS.isTagged(material);
    }

    public static boolean isCake(@NotNull Material material) {
        return material == Material.CAKE || Tag.CANDLE_CAKES.isTagged(material);
    }

    public static boolean isCauldron(@NotNull Material material) {
        return Tag.CAULDRONS.isTagged(material);
    }

    public static boolean isAnvil(@NotNull Material material) {
        return Tag.ANVIL.isTagged(material);
    }

    public static boolean isBed(@NotNull Material material) {
        return Tag.BEDS.isTagged(material);
    }

    public static boolean isPhysicalInteractionBlock(@NotNull Material material) {
        return isPressurePlate(material) || isTramplable(material) || isTripwire(material);
    }
}
