package su.nightexpress.excellentclaims.util.list;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.flag.FlagUtils;
import su.nightexpress.excellentclaims.util.ClaimUtils;
import su.nightexpress.nightcore.Engine;
import su.nightexpress.nightcore.language.LangAssets;
import su.nightexpress.nightcore.util.*;
import su.nightexpress.nightcore.util.bridge.RegistryType;
import su.nightexpress.nightcore.util.bukkit.NightItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListTypes {

    public static final ListType<Material>   USABLE_BLOCK  = forMaterial(material -> BlockUtil.isFunctional(material) && !FlagUtils.hasSpecificBlockFlag(material));
    public static final ListType<EntityType> USABLE_MOB    = forEntity(type -> ClaimUtils.isUsableEntity(type) && !FlagUtils.hasSpecificEntityInteractFlag(type));
    public static final ListType<EntityType> SPAWNABLE_MOB = forEntity(type -> type.isAlive() && type != EntityType.PLAYER);
    public static final ListType<DamageType> DAMAGE_TYPE   = forKeyed(RegistryType.DAMAGE_TYPE, LangAssets::get, ListTypes::getDamageIcon);
    public static final ListType<Command>    COMMAND       = forCommand();

    @NotNull
    private static ListType<Material> forMaterial(@NotNull Predicate<Material> predicate) {
        return forKeyed(RegistryType.MATERIAL, LangUtil::getSerializedName, ListTypes::getItemBlockIcon, predicate);
    }

    @NotNull
    private static ListType<EntityType> forEntity(@NotNull Predicate<EntityType> predicate) {
        return forKeyed(RegistryType.ENTITY_TYPE, LangUtil::getSerializedName, ListTypes::getEntityIcon, predicate);
    }

    @NotNull
    private static <T extends Keyed> ListType<T> forKeyed(@NotNull RegistryType<T> type, @NotNull Function<T, String> localizer, @NotNull Function<T, NightItem> icon) {
        return forKeyed(type, localizer, icon, v -> true);
    }

    @NotNull
    private static <T extends Keyed> ListType<T> forKeyed(@NotNull RegistryType<T> type,
                                                          @NotNull Function<T, String> localizer,
                                                          @NotNull Function<T, NightItem> icon,
                                                          @NotNull Predicate<T> predicate) {

        return new ListType<>() {

            @Override
            @NotNull
            public Set<SmartEntry<T>> getAllValues() {
                return BukkitThing.getAll(type).stream().filter(predicate).map(this::toEntry).collect(Collectors.toSet());
            }

            @Override
            @NotNull
            public Set<String> getAllValuesNames() {
                return Lists.modify(this.getAllValues(), SmartEntry::getName);
            }

            @Override
            @Nullable
            public T parse(@NotNull String string) {
                T value = BukkitThing.getByString(type, string);
                return value == null || !predicate.test(value) ? null : value;
            }

            @NotNull
            public SmartEntry<T> toEntry(@NotNull T value) {
                return new SmartEntry<>(value, icon.apply(value), this.getName(value), this.getNameLocalized(value));
            }

            @Override
            @NotNull
            public String getName(@NotNull T value) {
                return BukkitThing.getAsString(value);
            }

            @Override
            @NotNull
            public String getNameLocalized(@NotNull T value) {
                return localizer.apply(value);
            }
        };
    }

    @NotNull
    private static ListType<Command> forCommand() {

        return new ListType<>() {

            @Override
            @NotNull
            public Set<SmartEntry<Command>> getAllValues() {
                return Engine.software().getCommandMap().getCommands().stream().distinct().map(this::toEntry).collect(Collectors.toSet());
            }

            @Override
            @NotNull
            public Set<String> getAllValuesNames() {
                return Lists.modify(this.getAllValues(), SmartEntry::getName);
            }

            @Override
            @Nullable
            public Command parse(@NotNull String string) {
                return CommandUtil.getCommand(string).orElse(null);
            }

            @NotNull
            public SmartEntry<Command> toEntry(@NotNull Command value) {
                return new SmartEntry<>(value, NightItem.fromType(Material.MAP), this.getName(value), this.getNameLocalized(value));
            }

            @Override
            @NotNull
            public String getName(@NotNull Command command) {
                return command.getName();
            }

            @Override
            @NotNull
            public String getNameLocalized(@NotNull Command command) {
                return "/" + command.getName();
            }
        };
    }

    @NotNull
    private static NightItem getItemBlockIcon(@NotNull Material material) {
        return NightItem.fromType(material);
    }

    @NotNull
    private static NightItem getEntityIcon(@NotNull EntityType type) {
        if (Version.isAtLeast(Version.MC_1_21_6)) {
            if (type == EntityType.HAPPY_GHAST) return NightItem.asCustomHead("f7e12a5959992ce153ced236aa6732396531834d00f283bce1952d04f3591d99");
        }

        return switch (type) {
            case MINECART -> NightItem.fromType(Material.MINECART);
            case OAK_BOAT -> NightItem.fromType(Material.OAK_BOAT);
            case JUNGLE_BOAT -> NightItem.fromType(Material.JUNGLE_BOAT);
            case SPRUCE_BOAT -> NightItem.fromType(Material.SPRUCE_BOAT);
            case ACACIA_CHEST_BOAT -> NightItem.fromType(Material.ACACIA_CHEST_BOAT);
            case MANGROVE_BOAT -> NightItem.fromType(Material.MANGROVE_BOAT);
            case BIRCH_CHEST_BOAT -> NightItem.fromType(Material.BIRCH_CHEST_BOAT);
            case DARK_OAK_BOAT -> NightItem.fromType(Material.DARK_OAK_BOAT);
            case CHERRY_CHEST_BOAT -> NightItem.fromType(Material.CHERRY_CHEST_BOAT);
            case PALE_OAK_BOAT -> NightItem.fromType(Material.PALE_OAK_BOAT);
            case DARK_OAK_CHEST_BOAT -> NightItem.fromType(Material.DARK_OAK_CHEST_BOAT);
            case OAK_CHEST_BOAT -> NightItem.fromType(Material.OAK_CHEST_BOAT);
            case JUNGLE_CHEST_BOAT -> NightItem.fromType(Material.JUNGLE_CHEST_BOAT);
            case MANGROVE_CHEST_BOAT -> NightItem.fromType(Material.MANGROVE_CHEST_BOAT);
            case SPRUCE_CHEST_BOAT -> NightItem.fromType(Material.SPRUCE_CHEST_BOAT);
            case BAMBOO_RAFT -> NightItem.fromType(Material.BAMBOO_RAFT);
            case BAMBOO_CHEST_RAFT -> NightItem.fromType(Material.BAMBOO_CHEST_RAFT);
            case CHEST_MINECART -> NightItem.fromType(Material.CHEST_MINECART);
            case FURNACE_MINECART -> NightItem.fromType(Material.FURNACE_MINECART);
            case TNT_MINECART -> NightItem.fromType(Material.TNT_MINECART);
            case HOPPER_MINECART -> NightItem.fromType(Material.HOPPER_MINECART);
            case SPAWNER_MINECART -> NightItem.fromType(Material.SPAWNER);
            case PAINTING -> NightItem.fromType(Material.PAINTING);
            case BIRCH_BOAT -> NightItem.fromType(Material.BIRCH_BOAT);
            case ITEM_FRAME -> NightItem.fromType(Material.ITEM_FRAME);
            case ACACIA_BOAT -> NightItem.fromType(Material.ACACIA_BOAT);
            case CHERRY_BOAT -> NightItem.fromType(Material.CHERRY_BOAT);
            case COMMAND_BLOCK_MINECART -> NightItem.fromType(Material.COMMAND_BLOCK_MINECART);
            case PALE_OAK_CHEST_BOAT -> NightItem.fromType(Material.PALE_OAK_CHEST_BOAT);
            case END_CRYSTAL -> NightItem.fromType(Material.END_CRYSTAL);
            case GLOW_ITEM_FRAME -> NightItem.fromType(Material.GLOW_ITEM_FRAME);
            case PLAYER -> NightItem.fromType(Material.PLAYER_HEAD);
            case BAT -> NightItem.asCustomHead("6de75a2cc1c950e82f62abe20d42754379dfad6f5ff546e58f1c09061862bb92");
            case COD -> NightItem.asCustomHead("4feeff4b7fcfce68b0f74df0db0ad0c01f7301d0c6d893699b402bd50bb376b0");
            case CAMEL -> NightItem.asCustomHead("7eb6ad908b8d5155bc4d249271ef6084d455dd0e70a4002eb148f9e20b9deb2c");
            case SQUID -> NightItem.asCustomHead("464bdc6f600656511bef596c1a16aab1d3f5dbaae8bee19d5c04de0db21ce92c");
            case PARROT -> NightItem.asCustomHead("5df4b3401a4d06ad66ac8b5c4d189618ae617f9c143071c8ac39a563cf4e4208");
            case SALMON -> NightItem.asCustomHead("ef0b5ce781cf017224baccd9de367e41691b33ad4d84c03e49844052153465a7");
            case DOLPHIN -> NightItem.asCustomHead("c56f13b5f22d2a4a56e6773ff3f09524df086f4cadcbed543e6212725dfd05de");
            case SHULKER -> NightItem.asCustomHead("76640530d98db934fc5b955ea23c11c80c4fdad061001e8a2913e38390df69a6");
            case STRIDER -> NightItem.asCustomHead("e245e4760abf10f2900626914cf42f80440cd53099ae5529534f59824067dad6");
            case TADPOLE -> NightItem.asCustomHead("987035f5352334c2cba6ac4c65c2b9059739d6d0e839c1dd98d75d2e77957847");
            case GLOW_SQUID -> NightItem.asCustomHead("55e2b46e52ac92d419a2ddbcc9cdce7b451cb48ae739d85d607db0502a008ce0");
            case PUFFERFISH -> NightItem.asCustomHead("6df8c316962949ba3be445c94ebf714108252d46459b66110f4bc14e0e1b59dc");
            case TROPICAL_FISH -> NightItem.asCustomHead("d6dd5e6addb56acbc694ea4ba5923b1b25688178feffa72290299e2505c97281");
            case GUARDIAN -> NightItem.asCustomHead("b8e725779c234c590cce854db5c10485ed8d8a33fa9b2bdc3424b68bb1380bed");
            case ELDER_GUARDIAN -> NightItem.asCustomHead("30f868caf19cf2124f0fef98e6b8773d27fbf42d93aab06b22ee033b2aee6447");
            case ARMOR_STAND -> NightItem.fromType(Material.ARMOR_STAND);
            case ENDER_DRAGON -> NightItem.fromType(Material.DRAGON_HEAD);
            case ALLAY -> NightItem.asCustomHead("f315a6a899b15a9810bc2bfdffd491a038c48016c189d43f327092c8f011599f");
            case ARMADILLO -> NightItem.asCustomHead("9852b33ba294f560090752d113fe728cbc7dd042029a38d5382d65a2146068b7");
            case AXOLOTL -> NightItem.asCustomHead("21c3aa0d539208b47972bf8e72f0505cdcfb8d7796b2fcf85911ce94fd0193d0");
            case BEE -> NightItem.asCustomHead("cce9edbbc5fdc0d8487ac72eab239d2cacfe408d74288d6384b044111ba4de0f");
            case BLAZE -> NightItem.asCustomHead("b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0");
            case BOGGED -> NightItem.asCustomHead("a3b9003ba2d05562c75119b8a62185c67130e9282f7acbac4bc2824c21eb95d9");
            case BREEZE -> NightItem.asCustomHead("a275728af7e6a29c88125b675a39d88ae9919bb61fdc200337fed6ab0c49d65c");
            case CAT -> NightItem.asCustomHead("3a12188258601bcb7f76e3e2489555a26c0d76e6efec2fd966ca372b6dde00");
            case CAVE_SPIDER -> NightItem.asCustomHead("41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224");
            case CHICKEN -> NightItem.asCustomHead("1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893");
            case COW -> NightItem.asCustomHead("5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5");
            case CREAKING -> NightItem.asCustomHead("f93c9469797dd29ed877adefcb3d2e6da528d9203567ca2a4075e751db05c3e0");
            case CREEPER -> NightItem.asCustomHead("f4254838c33ea227ffca223dddaabfe0b0215f70da649e944477f44370ca6952");
            case DONKEY -> NightItem.asCustomHead("63a976c047f412ebc5cb197131ebef30c004c0faf49d8dd4105fca1207edaff3");
            case DROWNED -> NightItem.asCustomHead("c84df79c49104b198cdad6d99fd0d0bcf1531c92d4ab6269e40b7d3cbbb8e98c");
            case ENDERMAN -> NightItem.asCustomHead("7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf");
            case ENDERMITE -> NightItem.asCustomHead("5a1a0831aa03afb4212adcbb24e5dfaa7f476a1173fce259ef75a85855");
            case EVOKER -> NightItem.asCustomHead("d954135dc82213978db478778ae1213591b93d228d36dd54f1ea1da48e7cba6");
            case FOX -> NightItem.asCustomHead("d8954a42e69e0881ae6d24d4281459c144a0d5a968aed35d6d3d73a3c65d26a");
            case FROG -> NightItem.asCustomHead("ce62e8a048d040eb0533ba26a866cd9c2d0928c931c50b4482ac3a3261fab6f0");
            case GHAST -> NightItem.asCustomHead("8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0");
            case GOAT -> NightItem.asCustomHead("457a0d538fa08a7affe312903468861720f9fa34e86d44b89dcec5639265f03");
            case HORSE -> NightItem.asCustomHead("42eb967ab94fdd41a6325f1277d6dc019226e5cf34977eee69597fafcf5e");
            case HOGLIN -> NightItem.asCustomHead("9bb9bc0f01dbd762a08d9e77c08069ed7c95364aa30ca1072208561b730e8d75");
            case HUSK -> NightItem.asCustomHead("d674c63c8db5f4ca628d69a3b1f8a36e29d8fd775e1a6bdb6cabb4be4db121");
            case ILLUSIONER -> NightItem.asCustomHead("512512e7d016a2343a7bff1a4cd15357ab851579f1389bd4e3a24cbeb88b");
            case IRON_GOLEM -> NightItem.asCustomHead("89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714");
            case LLAMA -> NightItem.asCustomHead("cf24e56fd9ffd7133da6d1f3e2f455952b1da462686f753c597ee82299a");
            case MAGMA_CUBE -> NightItem.asCustomHead("38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429");
            case MULE -> NightItem.asCustomHead("a0486a742e7dda0bae61ce2f55fa13527f1c3b334c57c034bb4cf132fb5f5f");
            case MOOSHROOM -> NightItem.asCustomHead("a163bc416b8e6058f92b231e9a524b7fe118eb6e7eeab4ad16d1b52a3ec04fcd");
            case OCELOT -> NightItem.asCustomHead("5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1");
            case PANDA -> NightItem.asCustomHead("ba6e3ad823f96d4a80a14556d8c9c7632163bbd2a876c0118b458925d87a5513");
            case PHANTOM -> NightItem.asCustomHead("411d25bcdabafad5fd6e010c5b1cf7a00c9cca40c5a46747f706dc9cb3a");
            case PIG -> NightItem.asCustomHead("621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4");
            case PIGLIN -> NightItem.asCustomHead("11d18bbd0d795b9ac8efaad655e3d0c59fcbb9b964c2a9948ef537f4a3fbbf87");
            case PIGLIN_BRUTE -> NightItem.asCustomHead("3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf");
            case PILLAGER -> NightItem.asCustomHead("4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333");
            case POLAR_BEAR -> NightItem.asCustomHead("c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90");
            case RABBIT -> NightItem.asCustomHead("ffecc6b5e6ea5ced74c46e7627be3f0826327fba26386c6cc7863372e9bc");
            case RAVAGER -> NightItem.asCustomHead("3b62501cd1b87b37f628018210ec5400cb65a4d1aab74e6a3f7f62aa85db97ee");
            case SHEEP -> NightItem.asCustomHead("f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70");
            case SILVERFISH -> NightItem.asCustomHead("da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540");
            case SKELETON -> NightItem.asCustomHead("301268e9c492da1f0d88271cb492a4b302395f515a7bbf77f4a20b95fc02eb2");
            case SKELETON_HORSE -> NightItem.asCustomHead("72618a3c268ca388a38b893016aba26f6271519cde1a6bbd599cdd6472843b7f");
            case SLIME -> NightItem.asCustomHead("895aeec6b842ada8669f846d65bc49762597824ab944f22f45bf3bbb941abe6c");
            case SNIFFER -> NightItem.asCustomHead("fe5a8341c478a134302981e6a7758ea4ecfd8d62a0df4067897e75502f9b25de");
            case SNOW_GOLEM -> NightItem.asCustomHead("98e334e4bee04264759a766bc1955cfaf3f56201428fafec8d4bf1bb36ae6");
            case SPIDER -> NightItem.asCustomHead("cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1");
            case STRAY -> NightItem.asCustomHead("78ddf76e555dd5c4aa8a0a5fc584520cd63d489c253de969f7f22f85a9a2d56");
            case TRADER_LLAMA -> NightItem.asCustomHead("15ad6b69cc6b4769d3516a0ce98b99b2a5d406fea4912dec570ea4a4f2bcc0ff");
            case TURTLE -> NightItem.asCustomHead("99d6712582d60a0058e4fbee0e89dd089fbabb80f07e4a0874904c91bc48f08a");
            case VEX -> NightItem.asCustomHead("c2ec5a516617ff1573cd2f9d5f3969f56d5575c4ff4efefabd2a18dc7ab98cd");
            case VILLAGER -> NightItem.asCustomHead("4ca8ef2458a2b10260b8756558f7679bcb7ef691d41f534efea2ba75107315cc");
            case VINDICATOR -> NightItem.asCustomHead("6deaec344ab095b48cead7527f7dee61b063ff791f76a8fa76642c8676e2173");
            case WANDERING_TRADER -> NightItem.asCustomHead("499d585a9abf59fae277bb684d24070cef21e35609a3e18a9bd5dcf73a46ab93");
            case WARDEN -> NightItem.asCustomHead("10b3c91b727d87d8c8aa96028f2275b8405debc7516a023d0f7748bab21f9c43");
            case WITCH -> NightItem.asCustomHead("20e13d18474fc94ed55aeb7069566e4687d773dac16f4c3f8722fc95bf9f2dfa");
            case WITHER -> NightItem.asCustomHead("ee280cefe946911ea90e87ded1b3e18330c63a23af5129dfcfe9a8e166588041");
            case WITHER_SKELETON -> NightItem.asCustomHead("7953b6c68448e7e6b6bf8fb273d7203acd8e1be19e81481ead51f45de59a8");
            case WOLF -> NightItem.asCustomHead("69d1d3113ec43ac2961dd59f28175fb4718873c6c448dfca8722317d67");
            case ZOGLIN -> NightItem.asCustomHead("e67e18602e03035ad68967ce090235d8996663fb9ea47578d3a7ebbc42a5ccf9");
            case ZOMBIE, GIANT -> NightItem.asCustomHead("64528b3229660f3dfab42414f59ee8fd01e80081dd3df30869536ba9b414e089");
            case ZOMBIE_HORSE -> NightItem.asCustomHead("171ce469cba4426c811f69be5d958a09bfb9b1b2bb649d3577a0c2161ad2f524");
            case ZOMBIE_VILLAGER -> NightItem.asCustomHead("8c7505f224d5164a117d8c69f015f99eff434471c8a2df907096c4242c3524e8");
            case ZOMBIFIED_PIGLIN -> NightItem.asCustomHead("7eabaecc5fae5a8a49c8863ff4831aaa284198f1a2398890c765e0a8de18da8c");
            default -> NightItem.asCustomHead("c48d7d177f256ce10002ba9706068b93e337da24c85626c5af832021245f7a02"); // duck :D
        };
    }

    private static final Map<DamageType, Material> DAMAGE_ICONS = new HashMap<>();

    private static void addDamageIcon(@NotNull DamageType type, @NotNull Material material) {
        DAMAGE_ICONS.put(type, material);
    }

    static {
        addDamageIcon(DamageType.ARROW, Material.ARROW);
        addDamageIcon(DamageType.BAD_RESPAWN_POINT, Material.RESPAWN_ANCHOR);
        addDamageIcon(DamageType.CACTUS, Material.CACTUS);
        addDamageIcon(DamageType.CAMPFIRE, Material.CAMPFIRE);
        addDamageIcon(DamageType.CRAMMING, Material.ARMOR_STAND);
        addDamageIcon(DamageType.DRAGON_BREATH, Material.DRAGON_BREATH);
        addDamageIcon(DamageType.DROWN, Material.WATER_BUCKET);
        addDamageIcon(DamageType.DRY_OUT, Material.DIRT);
        addDamageIcon(DamageType.ENDER_PEARL, Material.ENDER_PEARL);
        addDamageIcon(DamageType.EXPLOSION, Material.TNT);
        addDamageIcon(DamageType.FALL, Material.FEATHER);
        addDamageIcon(DamageType.FALLING_ANVIL, Material.ANVIL);
        addDamageIcon(DamageType.FALLING_BLOCK, Material.SAND);
        addDamageIcon(DamageType.FALLING_STALACTITE, Material.POINTED_DRIPSTONE);
        addDamageIcon(DamageType.FIREBALL, Material.FIRE_CHARGE);
        addDamageIcon(DamageType.FIREWORKS, Material.FIREWORK_ROCKET);
        addDamageIcon(DamageType.FLY_INTO_WALL, Material.ELYTRA);
        addDamageIcon(DamageType.FREEZE, Material.POWDER_SNOW_BUCKET);
        addDamageIcon(DamageType.GENERIC, Material.SKELETON_SKULL);
        addDamageIcon(DamageType.GENERIC_KILL, Material.IRON_SWORD);
        addDamageIcon(DamageType.HOT_FLOOR, Material.MAGMA_BLOCK);
        addDamageIcon(DamageType.IN_FIRE, Material.FLINT_AND_STEEL);
        addDamageIcon(DamageType.IN_WALL, Material.GRAVEL);
        addDamageIcon(DamageType.INDIRECT_MAGIC, Material.POTION);
        addDamageIcon(DamageType.LAVA, Material.LAVA_BUCKET);
        addDamageIcon(DamageType.LIGHTNING_BOLT, Material.LIGHTNING_ROD);
        addDamageIcon(DamageType.MACE_SMASH, Material.MACE);
        addDamageIcon(DamageType.MAGIC, Material.WITCH_SPAWN_EGG);
        addDamageIcon(DamageType.MOB_ATTACK, Material.ZOMBIE_HEAD);
        addDamageIcon(DamageType.MOB_ATTACK_NO_AGGRO, Material.PIGLIN_HEAD);
        addDamageIcon(DamageType.MOB_PROJECTILE, Material.BOW);
        addDamageIcon(DamageType.ON_FIRE, Material.FLINT_AND_STEEL);
        addDamageIcon(DamageType.OUT_OF_WORLD, Material.STRUCTURE_VOID);
        addDamageIcon(DamageType.OUTSIDE_BORDER, Material.BARRIER);
        addDamageIcon(DamageType.PLAYER_ATTACK, Material.PLAYER_HEAD);
        addDamageIcon(DamageType.PLAYER_EXPLOSION, Material.END_CRYSTAL);
        addDamageIcon(DamageType.SONIC_BOOM, Material.SCULK_SENSOR);
        addDamageIcon(DamageType.SPIT, Material.LLAMA_SPAWN_EGG);
        addDamageIcon(DamageType.STALAGMITE, Material.POINTED_DRIPSTONE);
        addDamageIcon(DamageType.STARVE, Material.ROTTEN_FLESH);
        addDamageIcon(DamageType.STING, Material.BEE_SPAWN_EGG);
        addDamageIcon(DamageType.SWEET_BERRY_BUSH, Material.SWEET_BERRIES);
        addDamageIcon(DamageType.THORNS, Material.DIAMOND_CHESTPLATE);
        addDamageIcon(DamageType.THROWN, Material.EGG);
        addDamageIcon(DamageType.TRIDENT, Material.TRIDENT);
        addDamageIcon(DamageType.UNATTRIBUTED_FIREBALL, Material.FIRE_CHARGE);
        addDamageIcon(DamageType.WITHER, Material.WITHER_ROSE);
        addDamageIcon(DamageType.WITHER_SKULL, Material.WITHER_SKELETON_SKULL);
    }

    @NotNull
    private static NightItem getDamageIcon(@NotNull DamageType type) {
        return NightItem.fromType(DAMAGE_ICONS.getOrDefault(type, Material.IRON_SWORD));
    }
}
