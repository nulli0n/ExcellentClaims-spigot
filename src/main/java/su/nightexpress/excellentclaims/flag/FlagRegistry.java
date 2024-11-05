package su.nightexpress.excellentclaims.flag;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.flag.FlagCategory;
import su.nightexpress.excellentclaims.flag.impl.AbstractFlag;
import su.nightexpress.excellentclaims.flag.list.EntityFlags;
import su.nightexpress.excellentclaims.flag.list.NaturalFlags;
import su.nightexpress.excellentclaims.flag.list.PlayerFlags;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.*;
import java.util.stream.Collectors;

public class FlagRegistry {

    private static final String                       FILE_NAME        = "flags.yml";
    private static final Map<String, AbstractFlag<?>> FLAG_MAP         = new HashMap<>();
    private static final Set<AbstractFlag<?>>         DELAYED_REGISTER = new HashSet<>();

    private static ClaimPlugin plugin;
    private static FileConfig  config;

    public static void load(@NotNull ClaimPlugin plugin) {
        FlagRegistry.plugin = plugin;
        config = FileConfig.loadOrExtract(plugin, FILE_NAME);

        registerNaturalFlags();
        registerPlayerFlags();
        registerEntityFlags();
        registerDelayed();

        config.saveChanges();

        plugin.info("Registered " + FLAG_MAP.size() + " flags.");
    }

    private static void registerNaturalFlags() {
        register(NaturalFlags.AMETHYST_GROW);
        register(NaturalFlags.AMETHYST_FORM);
        register(NaturalFlags.BAMBOO_GROW);
        register(NaturalFlags.CACTUS_GROW);
        register(NaturalFlags.CROP_GROW);
        register(NaturalFlags.CORAL_DIE);
        register(NaturalFlags.FARMLAND_DRY);
        register(NaturalFlags.FIRE_BURN_OUT);
        register(NaturalFlags.FIRE_DAMAGE_BLOCKS);
        register(NaturalFlags.FIRE_SPREAD);
        register(NaturalFlags.GRASS_GROW);
        register(NaturalFlags.ICE_FORM);
        register(NaturalFlags.ICE_MELT);
        register(NaturalFlags.LAVA_FLOW);
        register(NaturalFlags.LEAF_DECAY);
        register(NaturalFlags.MUSHROOM_GROW);
        register(NaturalFlags.MYCELIUM_SPREAD);
        register(NaturalFlags.PISTON_USE);
        register(NaturalFlags.SNOW_FORM);
        register(NaturalFlags.SNOW_MELT);
        register(NaturalFlags.SUGAR_CANE_GROW);
        register(NaturalFlags.TNT_BLOCK_DAMAGE);
        register(NaturalFlags.TREE_GROW);
        register(NaturalFlags.TURTLE_EGG_HATCH);
        register(NaturalFlags.VINE_GROW);
        register(NaturalFlags.WATER_FLOW);
    }

    private static void registerPlayerFlags() {
        register(PlayerFlags.ARMOR_STAND_USE);
        register(PlayerFlags.BLOCK_BREAK);
        register(PlayerFlags.BLOCK_FERTILIZE);
        register(PlayerFlags.BLOCK_INTERACT_MODE);
        register(PlayerFlags.BLOCK_INTERACT_LIST);
        register(PlayerFlags.BLOCK_PLACE);
        register(PlayerFlags.BLOCK_TRAMPLING);
        register(PlayerFlags.CHEST_ACCESS);
        register(PlayerFlags.CHORUS_FRUIT_USE);
        register(PlayerFlags.CONTAINER_ACCESS);
        register(PlayerFlags.ENDER_PEARL_USE);
        register(PlayerFlags.END_PORTAL_USE);
        register(PlayerFlags.ENTITY_INTERACT_MODE);
        register(PlayerFlags.ENTITY_INTERACT_LIST);
        register(PlayerFlags.ITEM_USE_MODE);
        register(PlayerFlags.ITEM_USE_LIST);
        register(PlayerFlags.NETHER_PORTAL_USE);
        register(PlayerFlags.PLAYER_DAMAGE_MODE);
        register(PlayerFlags.PLAYER_DAMAGE_LIST);
        register(PlayerFlags.PLAYER_DAMAGE_ANIMALS);
        register(PlayerFlags.PLAYER_DAMAGE_PLAYERS);
        register(PlayerFlags.PLAYER_DAMAGE_VILLAGERS);
        register(PlayerFlags.PLAYER_ITEM_DROP);
        register(PlayerFlags.PLAYER_ITEM_PICKUP);
        register(PlayerFlags.SPAWN_EGG_USE);
        register(PlayerFlags.USE_BUTTONS);
        register(PlayerFlags.USE_PLATES);
        register(PlayerFlags.USE_DOORS);
        register(PlayerFlags.USE_TRIPWIRES);
        register(PlayerFlags.VEHICLE_USE);
        register(PlayerFlags.VILLAGER_INTERACT);
    }

    private static void registerEntityFlags() {
        register(EntityFlags.ANIMAL_DAMAGE_MODE);
        register(EntityFlags.ANIMAL_DAMAGE_LIST);
        register(EntityFlags.ANIMAL_GRIEF);
        register(EntityFlags.ANIMAL_SPAWN);
        register(EntityFlags.CREEPER_BLOCK_DAMAGE);
        register(EntityFlags.ENDERMAN_GRIEF);
        register(EntityFlags.ENDER_DRAGON_GRIEF);
        register(EntityFlags.END_CRYSTAL_BLOCK_DAMAGE);
        register(EntityFlags.EXPLOSION_BLOCK_DAMAGE);
        register(EntityFlags.ENTITY_SPAWN_MODE);
        register(EntityFlags.ENTITY_SPAWN_LIST);
        register(EntityFlags.FIREBALL_BLOCK_DAMAGE);
        register(EntityFlags.MONSTER_DAMAGE_PLAYERS);
        register(EntityFlags.MONSTER_SPAWN);
        register(EntityFlags.RAVAGER_GRIEF);
        register(EntityFlags.SILVERFISH_INFEST);
        register(EntityFlags.SNOWMAN_TRAIL);
        register(EntityFlags.VILLAGER_FARM);
        register(EntityFlags.WITHER_BLOCK_DAMAGE);
    }

    public static void registerDelayed() {
        DELAYED_REGISTER.forEach(FlagRegistry::register);
        DELAYED_REGISTER.clear();
    }

    public static void shutdown() {
        FLAG_MAP.clear();
        DELAYED_REGISTER.clear();
        plugin = null;
        config = null;
    }

    @NotNull
    public static <T, F extends AbstractFlag<T>> F register(@NotNull F flag) {
        if (config == null || plugin == null) {
            DELAYED_REGISTER.add(flag);
            return flag;
        }

        flag.loadSettings(config, flag.getId());

        FLAG_MAP.put(flag.getId(), flag);
        //plugin.info("Registered flag: '" + flag.getId() + "' [" + flag.getCategory().name() + "].");

        return flag;
    }

    @Nullable
    public static AbstractFlag<?> getFlag(@NotNull String id) {
        return FLAG_MAP.get(id.toLowerCase());
    }

    @NotNull
    public static Set<AbstractFlag<?>> getFlags() {
        return new HashSet<>(FLAG_MAP.values());
    }

    @NotNull
    public static Set<AbstractFlag<?>> getFlags(@NotNull FlagCategory category) {
        return FLAG_MAP.values().stream().filter(flag -> flag.getCategory() == category).collect(Collectors.toSet());
    }

    @NotNull
    public static List<String> getFlagNames() {
        return new ArrayList<>(FLAG_MAP.keySet());
    }
}
