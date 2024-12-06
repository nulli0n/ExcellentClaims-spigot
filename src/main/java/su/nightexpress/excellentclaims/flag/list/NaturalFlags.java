package su.nightexpress.excellentclaims.flag.list;

import org.bukkit.Material;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.impl.list.BooleanFlag;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import static su.nightexpress.nightcore.util.text.tag.Tags.LIGHT_RED;
import static su.nightexpress.nightcore.util.text.tag.Tags.LIGHT_YELLOW;

public class NaturalFlags {

    public static final BooleanFlag AMETHYST_FORM = new BooleanFlag("amethyst_form", FlagCategory.NATURAL,
        true,
        new NightItem(Material.MEDIUM_AMETHYST_BUD),
        "Allows " + LIGHT_YELLOW.enclose("amethysts") + " to form", "on budding amethysts."
    );

    public static final BooleanFlag AMETHYST_GROW = new BooleanFlag("amethyst_grow", FlagCategory.NATURAL,
        true,
        new NightItem(Material.AMETHYST_CLUSTER),
        "Allows " + LIGHT_YELLOW.enclose("amethyst") + " buds to grow."
    );

    public static final BooleanFlag BAMBOO_GROW = new BooleanFlag("bamboo_grow", FlagCategory.NATURAL,
        true,
        "187503026512be2e417cdee3cd78339c4caf09cab52823686572d012e9d19802",
        "Allows " + LIGHT_YELLOW.enclose("bamboo") + " to grow."
    );

    public static final BooleanFlag CACTUS_GROW = new BooleanFlag("cactus_grow", FlagCategory.NATURAL,
        true,
        "612de3c19542473b7b3441dfe7dd360d242ed30da76b4853d8a2417b85523ffc",
        "Allows " + LIGHT_YELLOW.enclose("cactus") + " to grow."
    );

    public static final BooleanFlag CORAL_DIE = new BooleanFlag("coral_die", FlagCategory.NATURAL,
        true,
        new NightItem(Material.FIRE_CORAL),
        "Allows " + LIGHT_YELLOW.enclose("corals") + " to die."
    );

    public static final BooleanFlag CROP_GROW = new BooleanFlag("crop_grow", FlagCategory.NATURAL,
        true,
        "7aadb37dd556f9eefbb8ea54943e1f657fac45409e34cd99c810d4dd461bc633",
        "Allows " + LIGHT_YELLOW.enclose("crops") + " to grow."
    );

    public static final BooleanFlag FARMLAND_DRY       = new BooleanFlag("farmland_dry", FlagCategory.NATURAL,
        true,
        "9a656926adcd507ff079ce42f5177435c28ef369359cf7ca6f9d825f5767db",
        "Allows " + LIGHT_YELLOW.enclose("farmlands") + " to dry."
    );

    public static final BooleanFlag FIRE_BURN_OUT      = new BooleanFlag("fire_burn_out", FlagCategory.NATURAL,
        true,
        "f764994d7eae1de63a8340eeb69d06ded7f65752de6c5d57f072355f05b1641c",
        "Allows " + LIGHT_YELLOW.enclose("fire") + " to burn out."
    );

    public static final BooleanFlag FIRE_DAMAGE_BLOCKS = new BooleanFlag("fire_damage_blocks", FlagCategory.NATURAL,
        true,
        "f764994d7eae1de63a8340eeb69d06ded7f65752de6c5d57f072355f05b1641c",
        "Allows " + LIGHT_YELLOW.enclose("fire") + " to burn blocks inside."
    );

    public static final BooleanFlag FIRE_SPREAD        = new BooleanFlag("fire_spread", FlagCategory.NATURAL,
        true,
        "f764994d7eae1de63a8340eeb69d06ded7f65752de6c5d57f072355f05b1641c",
        "Allows " + LIGHT_YELLOW.enclose("fire") + " to spread inside."
    );

    public static final BooleanFlag GRASS_GROW = new BooleanFlag("grass_grow", FlagCategory.NATURAL,
        true,
        "5eaa9ac15758d5177a896605985e98beac8fee0e6b2c68a8dc1f3c91c079fb89",
        "Allows " + LIGHT_YELLOW.enclose("grass") + " to form on dirt blocks."
    );

    public static final BooleanFlag ICE_FORM = new BooleanFlag("ice_form", FlagCategory.NATURAL,
        true,
        "ff83f255c35605bf2eb7d314d1f150c16f86ad0c313349fd1c08d4ddbbfe5957",
        "Allows " + LIGHT_YELLOW.enclose("iec") + " to form in snow biomes."
    );

    public static final BooleanFlag ICE_MELT = new BooleanFlag("ice_melt", FlagCategory.NATURAL,
        true,
        "a2644071b6c7bbae7b5e45d9f82f96ffb5ee8e177a23b825a4465607f1c9c",
        "Allows " + LIGHT_YELLOW.enclose("iec") + " to melt."
    );

    public static final BooleanFlag LEAF_DECAY = new BooleanFlag("leaf_decay", FlagCategory.NATURAL,
        true,
        "2c643033595753fb754cf72146e2e69d29b4de70cf3600a48010e73172ea850b",
        "Allows " + LIGHT_YELLOW.enclose("leaf") + " to decay."
    );

    public static final BooleanFlag MUSHROOM_GROW = new BooleanFlag("mushroom_grow", FlagCategory.NATURAL,
        true,
        "443223b6c29ef652c3636af776d89466e9f697f2565b9674aa8de0e9c36d7",
        "Allows " + LIGHT_YELLOW.enclose("mushrooms") + " to grow naturally."
    );

    public static final BooleanFlag MYCELIUM_SPREAD = new BooleanFlag("mycelium_spread", FlagCategory.NATURAL,
        true,
        "7eb4c41f481e816cf4b507b0a17595f2ba1f24664dc432be347d4e7a4eb3",
        "Allows " + LIGHT_YELLOW.enclose("mycelium") + " to spread inside."
    );

    public static final BooleanFlag PISTON_USE = new BooleanFlag("piston_use", FlagCategory.NATURAL,
        true,
        "80638282da1635642cc4b4b36ef03c293c8d3e468603ac1ab441223372451499",
        "Allows " + LIGHT_YELLOW.enclose("pistons") + " usage."
    );

    public static final BooleanFlag SNOW_FORM  = new BooleanFlag("snow_form", FlagCategory.NATURAL,
        true,
        "ba908f66cdf7971be07a7a1a4e9408f4cad98b2dd1ed0d472616c64a49f047ca",
        "Allows " + LIGHT_YELLOW.enclose("snow") + " to form during snowfall."
    );

    public static final BooleanFlag SNOW_MELT  = new BooleanFlag("snow_melt", FlagCategory.NATURAL,
        true,
        "482ed53d97e628cb30c9a422d5785857b5fcfbe68961e07fcb50f4e6c105201b",
        "Allows " + LIGHT_YELLOW.enclose("snow") + " to melt."
    );

    public static final BooleanFlag SUGAR_CANE_GROW = new BooleanFlag("sugar_cane_grow", FlagCategory.NATURAL,
        true,
        "be7aa9dce8b2c6727d918cb7b2ff69d547d32781a77f3be6423d1ff0401b8cef",
        "Allows " + LIGHT_YELLOW.enclose("sugar canes") + " to grow."
    );

    public static final BooleanFlag TNT_BLOCK_DAMAGE = new BooleanFlag("tnt_block_damage", FlagCategory.NATURAL,
        false,
        "dc75cd6f9c713e9bf43fea963990d142fc0d252974ebe04b2d882166cbb6d294",
        "Allows TNT to " + LIGHT_RED.enclose("destroy") + " blocks."
    );

    public static final BooleanFlag TREE_GROW = new BooleanFlag("tree_grow", FlagCategory.NATURAL,
        true,
        "97b245f3f150ba621675916a29d00690990ddd8de603293dd2f5f80070a2908",
        "Allows " + LIGHT_YELLOW.enclose("trees") + " to grow."
    );

    public static final BooleanFlag TURTLE_EGG_HATCH = new BooleanFlag("turtle_egg_hatch", FlagCategory.NATURAL,
        true,
        "77e90681f5aa18b17342479009877128f89974fbd24d381f7fceea79c915d0d2",
        "Allows " + LIGHT_YELLOW.enclose("turtle eggs") + " to hatch."
    );

    public static final BooleanFlag VINE_GROW  = new BooleanFlag("vine_grow", FlagCategory.NATURAL,
        true,
        new NightItem(Material.VINE),
            "Allows " + LIGHT_YELLOW.enclose("vines") + " and " + LIGHT_YELLOW.enclose("kelp") + " to grow."
    );

    public static final BooleanFlag WATER_FLOW = new BooleanFlag("water_flow", FlagCategory.NATURAL,
        false,
        "9686a68ed91d5405f6175b8c0372e0d315c0723c30d35e664c33de893339f688",
        "Allows " + LIGHT_YELLOW.enclose("water") + " to flow into claim."
    );

    public static final BooleanFlag LAVA_FLOW  = new BooleanFlag("lava_flow", FlagCategory.NATURAL,
        false,
        "967963cab657d2549dea534be238499d64d6165a592ca361512687a96ed960",
        "Allows " + LIGHT_YELLOW.enclose("lava") + " to flow into claim."
    );

    // TODO Flags for hoppers
}
