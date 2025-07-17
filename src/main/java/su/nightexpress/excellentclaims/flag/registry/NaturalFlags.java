package su.nightexpress.excellentclaims.flag.registry;

import org.bukkit.Material;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.FlagTypes;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;

import static su.nightexpress.nightcore.util.text.tag.Tags.LIGHT_YELLOW;

public class NaturalFlags {

    public static final ClaimFlag<Boolean> AMETHYST_FORM = ClaimFlag.builder("amethyst_form", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.MEDIUM_AMETHYST_BUD)
        .description("Whether " + LIGHT_YELLOW.wrap("amethysts") + " can form", "on budding amethysts.")
        .build();

    public static final ClaimFlag<Boolean> AMETHYST_GROW = ClaimFlag.builder("amethyst_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.AMETHYST_CLUSTER)
        .description("Whether " + LIGHT_YELLOW.wrap("amethyst") + " buds can grow.")
        .build();

    public static final ClaimFlag<Boolean> BAMBOO_GROW = ClaimFlag.builder("bamboo_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.BAMBOO)
        .description("Whether " + LIGHT_YELLOW.wrap("bamboo") + " can grow.")
        .build();

    public static final ClaimFlag<Boolean> CACTUS_GROW = ClaimFlag.builder("cactus_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.CACTUS)
        .description("Whether " + LIGHT_YELLOW.wrap("cactus") + " can grow.")
        .build();

    public static final ClaimFlag<Boolean> CORAL_DIE = ClaimFlag.builder("coral_die", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.FIRE_CORAL)
        .description("Whether " + LIGHT_YELLOW.wrap("corals") + " can die.")
        .build();

    public static final ClaimFlag<Boolean> CROP_GROW = ClaimFlag.builder("crop_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.WHEAT_SEEDS)
        .description("Whether " + LIGHT_YELLOW.wrap("crops") + " can grow.")
        .build();

    public static final ClaimFlag<Boolean> EXPLOSION_BLOCK_DAMAGE = ClaimFlag.builder("explosion_block_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.ENTITY)
        .defaultValue(false)
        .icon(Material.FIRE_CHARGE)
        .description("Whether " + LIGHT_YELLOW.wrap("explosions") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> FARMLAND_DRY = ClaimFlag.builder("farmland_dry", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.FARMLAND)
        .description("Whether " + LIGHT_YELLOW.wrap("farmlands") + " can dry.")
        .build();

    public static final ClaimFlag<Boolean> FIRE_BURN_OUT = ClaimFlag.builder("fire_burn_out", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.FLINT_AND_STEEL)
        .description("Whether " + LIGHT_YELLOW.wrap("fire") + " can burn out.")
        .build();

    public static final ClaimFlag<Boolean> FIRE_DAMAGE_BLOCKS = ClaimFlag.builder("fire_damage_blocks", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.FLINT_AND_STEEL)
        .description("Whether " + LIGHT_YELLOW.wrap("fire") + " can burn blocks.")
        .build();

    public static final ClaimFlag<Boolean> FIRE_SPREAD = ClaimFlag.builder("fire_spread", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.FLINT_AND_STEEL)
        .description("Whether " + LIGHT_YELLOW.wrap("fire") + " can spread.")
        .build();

    public static final ClaimFlag<Boolean> GRASS_GROW = ClaimFlag.builder("grass_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.GRASS_BLOCK)
        .description("Whether " + LIGHT_YELLOW.wrap("grass") + " can form from dirt.")
        .build();

    public static final ClaimFlag<Boolean> ICE_FORM = ClaimFlag.builder("ice_form", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.ICE)
        .description("Whether " + LIGHT_YELLOW.wrap("ice") + " can form in snow biomes.")
        .build();

    public static final ClaimFlag<Boolean> ICE_MELT = ClaimFlag.builder("ice_melt", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.BLUE_ICE)
        .description("Whether " + LIGHT_YELLOW.wrap("ice") + " can melt.")
        .build();

    public static final ClaimFlag<Boolean> LEAF_DECAY = ClaimFlag.builder("leaf_decay", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.OAK_LEAVES)
        .description("Whether " + LIGHT_YELLOW.wrap("leaf") + " can decay.")
        .build();

    public static final ClaimFlag<Boolean> MUSHROOM_GROW = ClaimFlag.builder("mushroom_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.RED_MUSHROOM)
        .description("Whether " + LIGHT_YELLOW.wrap("mushrooms") + " can grow naturally.")
        .build();

    public static final ClaimFlag<Boolean> MYCELIUM_SPREAD = ClaimFlag.builder("mycelium_spread", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.MYCELIUM)
        .description("Whether " + LIGHT_YELLOW.wrap("mycelium") + " can spread.")
        .build();

    public static final ClaimFlag<Boolean> PISTON_USE = ClaimFlag.builder("piston_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.PISTON)
        .description("Whether " + LIGHT_YELLOW.wrap("pistons") + " can work.")
        .build();

    public static final ClaimFlag<Boolean> SNOW_FORM = ClaimFlag.builder("snow_form", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.SNOW_BLOCK)
        .description("Whether " + LIGHT_YELLOW.wrap("snow") + " can form during snowfall.")
        .build();

    public static final ClaimFlag<Boolean> SNOW_MELT = ClaimFlag.builder("snow_melt", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.SNOW)
        .description("Whether " + LIGHT_YELLOW.wrap("snow") + " can melt.")
        .build();

    public static final ClaimFlag<Boolean> SUGAR_CANE_GROW = ClaimFlag.builder("sugar_cane_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.SUGAR_CANE)
        .description("Whether " + LIGHT_YELLOW.wrap("sugar cane") + " can grow.")
        .build();

    public static final ClaimFlag<Boolean> TNT_BLOCK_DAMAGE = ClaimFlag.builder("tnt_block_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(false)
        .icon(Material.TNT)
        .description("Whether " + LIGHT_YELLOW.wrap("TNT") + " can destroy blocks.")
        .build();

    public static final ClaimFlag<Boolean> TREE_GROW = ClaimFlag.builder("tree_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.OAK_SAPLING)
        .description("Whether " + LIGHT_YELLOW.wrap("trees") + " can grow.")
        .build();

    public static final ClaimFlag<Boolean> TURTLE_EGG_HATCH = ClaimFlag.builder("turtle_egg_hatch", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.TURTLE_EGG)
        .description("Whether " + LIGHT_YELLOW.wrap("turtle eggs") + " can hatch.")
        .build();

    public static final ClaimFlag<Boolean> VINE_GROW = ClaimFlag.builder("vine_grow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(true)
        .icon(Material.VINE)
        .description("Whether " + LIGHT_YELLOW.wrap("vines") + " and " + LIGHT_YELLOW.wrap("kelp") + " can grow.")
        .build();

    public static final ClaimFlag<Boolean> WATER_FLOW = ClaimFlag.builder("water_flow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(false)
        .icon(Material.WATER_BUCKET)
        .description("Whether " + LIGHT_YELLOW.wrap("water") + " can flow into claim.")
        .build();

    public static final ClaimFlag<Boolean> LAVA_FLOW = ClaimFlag.builder("lava_flow", FlagTypes.BOOLEAN)
        .category(FlagCategory.NATURAL)
        .defaultValue(false)
        .icon(Material.LAVA_BUCKET)
        .description("Whether " + LIGHT_YELLOW.wrap("lava") + " can flow into claim.")
        .build();

    // TODO Flags for hoppers
}
