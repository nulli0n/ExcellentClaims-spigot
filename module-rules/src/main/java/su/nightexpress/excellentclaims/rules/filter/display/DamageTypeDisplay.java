package su.nightexpress.excellentclaims.rules.filter.display;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.filter.ElementDisplay;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class DamageTypeDisplay implements ElementDisplay<DamageType> {

    public static final DamageTypeDisplay INSTANCE = new DamageTypeDisplay();

    private static final Map<DamageType, Material> DAMAGE_ICONS = new HashMap<>();

    @Override
    public String getNameLocalized(DamageType type) {
        return CoreLang.DAMAGE_TYPE.getLocalized(type);
    }

    @Override
    public @Nullable String getSpriteTag(DamageType value) {
        Material icon = DAMAGE_ICONS.get(value);
        if (icon == null) return null;

        return TagWrappers.SPRITE_ITEM.apply(icon);
    }

    private static void addDamageIcon(DamageType type, Material material) {
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
}
