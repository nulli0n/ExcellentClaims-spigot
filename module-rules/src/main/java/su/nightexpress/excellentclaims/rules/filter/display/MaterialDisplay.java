package su.nightexpress.excellentclaims.rules.filter.display;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.filter.ElementDisplay;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class MaterialDisplay implements ElementDisplay<Material> {

    public static final MaterialDisplay INSTANCE = new MaterialDisplay();

    private static final Map<Material, String> SPRITE_MAPPINGS = new HashMap<>();

    @Override
    public String getNameLocalized(Material value) {
        return LangUtil.getSerializedName(value);
    }

    @Override
    public @Nullable String getSpriteTag(Material item) {
        String sprite = SPRITE_MAPPINGS.get(item);
        if (sprite != null) return sprite;

        Set<Material> itemSprites = Lists.newSet(
            Material.BELL, Material.BREWING_STAND, Material.CAKE, Material.CAMPFIRE,
            Material.COMPARATOR, Material.FLOWER_POT, Material.HOPPER, Material.REPEATER,
            Material.SOUL_CAMPFIRE
        );

        Set<Material> blockSprites = Lists.newSet(
            Material.BEACON, Material.NOTE_BLOCK, Material.SPAWNER, Material.TRIPWIRE_HOOK
        );

        if (itemSprites.contains(item)) {
            return TagWrappers.SPRITE_ITEM.apply(item);
        }

        if (blockSprites.contains(item)) {
            return TagWrappers.SPRITE_BLOCK.apply(item);
        }

        return null;
    }

    static {
        addBlockSprite(Material.BARREL, "block/barrel_top");
        addBlockSprite(Material.BEEHIVE, "block/beehive_front");
        addBlockSprite(Material.LECTERN, "block/lectern_top");
        addBlockSprite(Material.BLAST_FURNACE, "block/blast_furnace_front");
        addBlockSprite(Material.CARTOGRAPHY_TABLE, "block/cartography_table_top");
        addBlockSprite(Material.CHISELED_BOOKSHELF, "block/chiseled_bookshelf_occupied");
        addBlockSprite(Material.COMPOSTER, "block/composter_side");
        addBlockSprite(Material.CRAFTER, "block/crafter_top_crafting");
        addBlockSprite(Material.CRAFTING_TABLE, "block/crafting_table_front");
        addBlockSprite(Material.DAYLIGHT_DETECTOR, "block/daylight_detector_top");
        addItemSprite(Material.DECORATED_POT, "item/flower_pot");
        addBlockSprite(Material.DISPENSER, "block/dispenser_front");
        addBlockSprite(Material.DROPPER, "block/dropper_front");
        addBlockSprite(Material.ENCHANTING_TABLE, "block/enchanting_table_side");
        addBlockSprite(Material.END_PORTAL_FRAME, "block/end_portal_frame_side");
        addBlockSprite(Material.FLETCHING_TABLE, "block/fletching_table_front");
        addBlockSprite(Material.FURNACE, "block/furnace_front");
        addBlockSprite(Material.GRINDSTONE, "block/grindstone_round");
        addBlockSprite(Material.JUKEBOX, "block/jukebox_side");
        addBlockSprite(Material.LODESTONE, "block/lodestone_side");
        addBlockSprite(Material.LOOM, "block/loom_top");
        addBlockSprite(Material.OBSERVER, "block/observer_front");
        addBlockSprite(Material.RESPAWN_ANCHOR, "block/respawn_anchor_side4");
        addBlockSprite(Material.SMITHING_TABLE, "block/smithing_table_front");
        addBlockSprite(Material.SMOKER, "block/smoker_front");
        addBlockSprite(Material.STONECUTTER, "block/stonecutter_side");
        addBlockSprite(Material.SUSPICIOUS_GRAVEL, "block/suspicious_gravel_0");
        addBlockSprite(Material.SUSPICIOUS_SAND, "block/suspicious_sand_0");
        addBlockSprite(Material.TARGET, "block/target_side");
        addBlockSprite(Material.TNT, "block/tnt_side");
        addBlockSprite(Material.TRIAL_SPAWNER, "block/trial_spawner_side_active");
        addBlockSprite(Material.VAULT, "block/vault_front_on");
    }

    private static void addBlockSprite(Material material, String spriteName) {
        SPRITE_MAPPINGS.put(material, TagWrappers.SPRITE_BLOCKS.apply(spriteName));
    }

    private static void addItemSprite(Material material, String spriteName) {
        SPRITE_MAPPINGS.put(material, TagWrappers.SPRITE_ITEMS.apply(spriteName));
    }
}
