package su.nightexpress.excellentclaims.flag.registry;

import org.bukkit.Color;
import org.bukkit.Material;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.FlagTypes;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import static su.nightexpress.nightcore.util.text.tag.Tags.*;

public class PlayerFlags {

    public static final ClaimFlag<Boolean> BLOCK_BREAK = ClaimFlag.builder("block_break", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.IRON_PICKAXE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can break blocks.")
        .build();

    public static final ClaimFlag<Boolean> BLOCK_FERTILIZE = ClaimFlag.builder("block_fertilize", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.BONE_MEAL)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can fertilize crops.")
        .build();

    public static final ClaimFlag<Boolean> BLOCK_HARVEST = ClaimFlag.builder("block_harvest", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.SWEET_BERRIES)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can harvest plants on right-click.")
        .build();

    public static final ClaimFlag<Boolean> BLOCK_INTERACT = ClaimFlag.builder("block_interact", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.NOTE_BLOCK)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use functional blocks.")
        .build();

    public static final ClaimFlag<Boolean> BLOCK_PLACE = ClaimFlag.builder("block_place", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.OAK_PLANKS)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can place blocks.")
        .build();

    public static final ClaimFlag<Boolean> BLOCK_TRAMPLING = ClaimFlag.builder("block_trampling", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.FARMLAND)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can trample crops and eggs.")
        .build();

    public static final ClaimFlag<Boolean> CHEST_ACCESS = ClaimFlag.builder("chest_access", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.CHEST)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can open chests.")
        .build();

    public static final ClaimFlag<Boolean> CONTAINER_ACCESS = ClaimFlag.builder("container_access", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.FURNACE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can open containers.")
        .build();

    public static final ClaimFlag<Boolean> EAT_CAKES = ClaimFlag.builder("eat_cakes", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.CAKE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can eat placed cakes.")
        .build();

    public static final ClaimFlag<Boolean> ENTITY_INTERACT = ClaimFlag.builder("entity_interact", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.SADDLE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can interact with entities.")
        .build();

//    public static final ClaimFlag<Boolean> ITEM_USE = ClaimFlag.builder("item_use", FlagTypes.BOOLEAN)
//        .category(FlagCategory.PLAYER)
//        .defaultValue(true)
//        .icon("ce67df8d715d1f0583c5955a83aa9ea0f13ed9826e1f70622e7a99cc8278e06d")
//        .description(
//            "Controls whether " + LIGHT_RED.wrap("non-members") + " can use",
//            "items in this claim."
//        )
//        .build();

    public static final ClaimFlag<Boolean> PLAYER_DAMAGE = ClaimFlag.builder("player_damage", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(true)
        .icon(Material.SKELETON_SKULL)
        .description("Whether " + LIGHT_YELLOW.wrap("all players") + " can be damaged.")
        .build();

    public static final ClaimFlag<Boolean> PLAYER_DAMAGE_ANIMALS = ClaimFlag.builder("player_damage_animals", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.WOODEN_SWORD)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can damage animals.")
        .build();

    public static final ClaimFlag<Boolean> PLAYER_DAMAGE_ENTITIES = ClaimFlag.builder("player_damage_entities", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.GOLDEN_SWORD)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can damage entities.")
        .build();

    public static final ClaimFlag<Boolean> PLAYER_DAMAGE_VILLAGERS = ClaimFlag.builder("player_damage_villagers", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.VILLAGER_SPAWN_EGG)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can damage villagers.")
        .build();

    public static final ClaimFlag<Boolean> PLAYER_ITEM_DROP = ClaimFlag.builder("player_item_drop", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(true)
        .icon(Material.STICK)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can drop items.")
        .build();

    public static final ClaimFlag<Boolean> PLAYER_ITEM_PICKUP = ClaimFlag.builder("player_item_pickup", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(true)
        .icon(Material.HOPPER)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can pickup items.")
        .build();

    public static final ClaimFlag<Boolean> PLAYER_DAMAGE_PLAYERS = ClaimFlag.builder("pvp", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.IRON_SWORD)
        .description("Whether " + LIGHT_YELLOW.wrap("PvP") + " is allowed.")
        .build();

    public static final ClaimFlag<Boolean> PLACE_FIREWORKS = ClaimFlag.builder("place_fireworks", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.FIREWORK_ROCKET)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can place fireworks.")
        .build();

    public static final ClaimFlag<Boolean> SHOOT_BOWS = ClaimFlag.builder("shoot_bows", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.BOW)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can shoot", "bows and crossbows.")
        .build();

    public static final ClaimFlag<Boolean> THROW_EGGS = ClaimFlag.builder("throw_eggs", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.EGG)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can throw eggs.")
        .build();

    public static final ClaimFlag<Boolean> THROW_ENDER_EYES = ClaimFlag.builder("throw_ender_eyes", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.ENDER_EYE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can throw ender eyes.")
        .build();

    public static final ClaimFlag<Boolean> THROW_POTIONS = ClaimFlag.builder("throw_potions", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(NightItem.fromType(Material.SPLASH_POTION).setColor(Color.GREEN))
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can throw potions.")
        .build();

    public static final ClaimFlag<Boolean> THROW_TRIDENTS = ClaimFlag.builder("throw_tridents", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.TRIDENT)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can throw tridents.")
        .build();

    public static final ClaimFlag<Boolean> THROW_WIND_CHARGES = ClaimFlag.builder("throw_wind_charges", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.WIND_CHARGE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can throw wind charges.")
        .build();



    public static final ClaimFlag<Boolean> USE_ANVILS = ClaimFlag.builder("use_anvils", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.ANVIL)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use anvils.")
        .build();

    public static final ClaimFlag<Boolean> USE_ARMOR_STAND = ClaimFlag.builder("armorstand_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.ARMOR_STAND)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use armor stands.")
        .build();

    public static final ClaimFlag<Boolean> USE_BEDS = ClaimFlag.builder("use_beds", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.RED_BED)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use beds.")
        .build();

    public static final ClaimFlag<Boolean> USE_BUTTONS = ClaimFlag.builder("use_buttons", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.OAK_BUTTON)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use buttons and levers.")
        .build();

    public static final ClaimFlag<Boolean> USE_CAULDRONS = ClaimFlag.builder("use_cauldrons", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.CAULDRON)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use cauldrons.")
        .build();

    public static final ClaimFlag<Boolean> USE_CHORUS_FRUIT = ClaimFlag.builder("chorus_fruit_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.CHORUS_FRUIT)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use chorus fruits.")
        .build();

    public static final ClaimFlag<Boolean> USE_COMMANDS = ClaimFlag.builder("use_commands", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(true)
        .icon(Material.COMMAND_BLOCK)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use commands.")
        .build();

    public static final ClaimFlag<Boolean> USE_DOORS = ClaimFlag.builder("use_doors", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.OAK_DOOR)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use doors, gates and trapdoors.")
        .build();

    public static final ClaimFlag<Boolean> USE_ENDER_PEARLS = ClaimFlag.builder("ender_pearl_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.ENDER_PEARL)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use ender pearls.")
        .build();

    public static final ClaimFlag<Boolean> USE_END_PORTAL = ClaimFlag.builder("end_portal_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.END_PORTAL_FRAME)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use end portals.")
        .build();

    public static final ClaimFlag<Boolean> USE_HORNS = ClaimFlag.builder("use_horns", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(true)
        .icon(Material.GOAT_HORN)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use goat horns.")
        .build();

    public static final ClaimFlag<Boolean> USE_LIGHTER = ClaimFlag.builder("use_lighter", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.FLINT_AND_STEEL)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use flint and steel.")
        .build();

    public static final ClaimFlag<Boolean> USE_NETHER_PORTAL = ClaimFlag.builder("nether_portal_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(true)
        .icon(Material.OBSIDIAN)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use nether portals.")
        .build();

    public static final ClaimFlag<Boolean> USE_PLATES = ClaimFlag.builder("use_plates", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.OAK_PRESSURE_PLATE)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use plates.")
        .build();

    public static final ClaimFlag<Boolean> USE_SIGNS = ClaimFlag.builder("use_signs", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.OAK_SIGN)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use and edit signs.")
        .build();

    public static final ClaimFlag<Boolean> USE_SPAWN_EGGS = ClaimFlag.builder("spawn_egg_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.CREEPER_SPAWN_EGG)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use " + LIGHT_YELLOW.wrap("spawn eggs") + ".")
        .build();

    public static final ClaimFlag<Boolean> USE_TRIPWIRES = ClaimFlag.builder("use_tripwires", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.TRIPWIRE_HOOK)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can trigger tripwires.")
        .build();

    public static final ClaimFlag<Boolean> USE_VEHICLES = ClaimFlag.builder("vehicle_use", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.MINECART)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use vehicles.")
        .build();

    public static final ClaimFlag<Boolean> USE_VILLAGERS = ClaimFlag.builder("villager_interact", FlagTypes.BOOLEAN)
        .category(FlagCategory.PLAYER)
        .defaultValue(false)
        .icon(Material.EMERALD)
        .description("Whether " + LIGHT_RED.wrap("non-members") + " can use villagers.")
        .build();
}
