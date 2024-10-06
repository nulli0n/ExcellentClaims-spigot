package su.nightexpress.excellentclaims.config;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.N;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.selection.visual.HighlightType;
import su.nightexpress.excellentclaims.util.ClaimUtils;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.RankMap;

import java.util.Map;
import java.util.Set;

import static su.nightexpress.excellentclaims.Placeholders.*;

public class Config {

    public static final String DIR_UI           = "/ui/";
    public static final String DIR_CLAIM_CHUNK  = "/claim/chunk/";
    public static final String DIR_CLAIM_REGION = "/claim/region/";

    public static final ConfigValue<Integer> GENERAL_SAVE_INTERVAL = ConfigValue.create("General.Save_Interval",
        600,
        "Sets how often (in seconds) modified claims will save their changes on the disk.",
        "[Asynchronous]",
        "[Default is 600 (10 minutes)]"
    );

    public static final ConfigValue<Boolean> GENERAL_UPDATE_PLAYER_NAMES = ConfigValue.create("General.UpdatePlayerNames",
        false,
        "Sets whether or not plugin will update player's name stored in their regions & claims on join.",
        "It's only useful if you have players that changes their names frequently.",
        "[Useless for offline mode]",
        "[Asynchronous]",
        "[Default is false]"
    );

    public static final ConfigValue<Boolean> GENERAL_ALLOW_REGION_TO_CHUNK_OVERLAP = ConfigValue.create("General.Allow_RegionToChunk_Overlap",
        true,
        "Allows to claim regions if they intersects with chunks claimed by the same player.",
        "This setting does not allow to claim chunks that intersects with regions.",
        "[Default is true]"
    );

    public static final ConfigValue<Boolean> GENERAL_ALLOW_CHUNK_TO_REGION_OVERLAP = ConfigValue.create("General.Allow_ChunkToRegion_Overlap",
        true,
        "Allows to claim chunks if they intersects with regions claimed by the same player.",
        "This setting does not allow to claim regions that intersects with claimed chunks.",
        "[Default is true]"
    );

    public static final ConfigValue<Integer> GENERAL_MAX_NAME_LENGTH = ConfigValue.create("General.Max_Name_Length",
        16,
        "Sets max. name length for claims when renaming."
    );

    public static final ConfigValue<Integer> GENERAL_MAX_DESCRIPTION_LENGTH = ConfigValue.create("General.Max_Description_Length",
        48,
        "Sets max. description length for claims when changing."
    );

    public static final ConfigValue<String[]> REGION_COMMAND_ALIASES = ConfigValue.create("Region.Command_Aliases",
        new String[]{"region", "rg"},
        "Custom aliases for region commands."
    );

    public static final ConfigValue<Set<String>> REGION_DISABLED_WORLDS = ConfigValue.create("Region.Disabled_Worlds",
        Lists.newSet("custom_world123", "your_world_name"),
        "List of worlds, where players can not create regions."
    ).onRead(set -> Lists.modify(set, String::toLowerCase));

    public static final ConfigValue<Integer> REGION_DEFAULT_PRIORITY = ConfigValue.create("Region.Default_Priority",
        5,
        "Sets default priority for newly claimed regions.",
        "Priority is mostly used to get the claim with the highest priority at player's location to determine if they can do specific actions there.",
        "By default claimed chunks has greater priority than regions.",
        "That means, that while players are inside claimed chunks that are inside a region, the chunk's settings/flags will take priority.",
        "Region priority can be changed at any time via region GUI or commands.",
        "[Default is 5]"
    );

    public static final ConfigValue<ItemStack> REGION_DEFAULT_ICON = ConfigValue.create("Region.Default_Icon",
        ClaimUtils.getDefaultIcon(ClaimType.REGION),
        "Icon used for new regions."
    );

    public static final ConfigValue<ItemStack> REGION_WAND_ITEM = ConfigValue.create("Region.WandItem",
        ClaimUtils.getDefaultSelectionItem(),
        "Item used to select region cuboids.",
        WIKI_ITEMS_URL
    );

    public static final ConfigValue<RankMap<Integer>> REGION_AMOUNT_PER_RANK = ConfigValue.create("Region.Amount_Per_Rank",
        (cfg, path, def) -> RankMap.readInt(cfg, path, 5),
        (cfg, path, map) -> map.write(cfg, path),
        () -> new RankMap<>(RankMap.Mode.RANK, Perms.PREFIX + "regions.", 5, Map.of(
            Placeholders.DEFAULT, 5,
            "vip", 6,
            "gold", 7,
            "admin", -1
        )),
        "Sets max. amount of regions per player based on their rank/permissions.",
        "Use -1 for unlimited amount."
    );

    public static final ConfigValue<String[]> LAND_COMMAND_ALIASES = ConfigValue.create("Land.Command_Aliases",
        new String[]{"land", "l"},
        "Custom aliases for land (chunk) commands."
    );

    public static final ConfigValue<Set<String>> LAND_DISABLED_WORLDS = ConfigValue.create("Land.Disabled_Worlds",
        Lists.newSet("custom_world123", "your_world_name"),
        "List of worlds, where players can not claim chunks."
    ).onRead(set -> Lists.modify(set, String::toLowerCase));

    public static final ConfigValue<Integer> LAND_DEFAULT_PRIORITY = ConfigValue.create("Land.Default_Priority",
        10,
        "Sets default priority for newly claimed chunks.",
        "Priority is mostly used to get the claim with the highest priority at player's location to determine if they can do specific actions there.",
        "By default claimed chunks has greater priority than regions.",
        "That means, that while players are inside claimed chunks that are inside a region, the chunk's settings/flags will take priority.",
        "Chunk priority can be changed at any time via region GUI or commands.",
        "[Default is 5]"
    );

    public static final ConfigValue<String> LAND_DEFAULT_NAME = ConfigValue.create("Land.Default_Name",
        PLAYER_NAME + "'s Claim",
        "Default name used for new claimed chunks.",
        "Use '" + PLAYER_NAME + "' for player name."
    );

    public static final ConfigValue<ItemStack> LAND_DEFAULT_ICON = ConfigValue.create("Land.Default_Icon",
        ClaimUtils.getDefaultIcon(ClaimType.CHUNK),
        "Icon used for new chunks."
    );

    public static final ConfigValue<ItemStack> LAND_MERGE_ITEM = ConfigValue.create("Land.MergeItem",
        ClaimUtils.getDefaultMergeItem(),
        "Item used to merge player's claims.",
        WIKI_ITEMS_URL
    );

    public static final ConfigValue<ItemStack> LAND_SEPARATE_ITEM = ConfigValue.create("Land.SeparateItem",
        ClaimUtils.getDefaultSeparateItem(),
        "Item used to separate player's claims.",
        WIKI_ITEMS_URL
    );

    public static final ConfigValue<RankMap<Integer>> LAND_AMOUNT_PER_RANK = ConfigValue.create("Land.Amount_Per_Rank",
        (cfg, path, def) -> RankMap.readInt(cfg, path, 5),
        (cfg, path, map) -> map.write(cfg, path),
        () -> new RankMap<>(RankMap.Mode.RANK, Perms.PREFIX + "chunks.", 5, Map.of(
            Placeholders.DEFAULT, 5,
            "vip", 6,
            "gold", 7,
            "admin", -1
        )),
        "Sets max. amount of claimed chunks per player based on their rank/permissions.",
        "Use -1 for unlimited amount."
    );

    public static final ConfigValue<RankMap<Integer>> LAND_CHUNKS_AMOUNT_PER_RANK = ConfigValue.create("Land.Chunks_Amount_Per_Rank",
        (cfg, path, def) -> RankMap.readInt(cfg, path, 5),
        (cfg, path, map) -> map.write(cfg, path),
        () -> new RankMap<>(RankMap.Mode.RANK, Perms.PREFIX + "chunks.", 5, Map.of(
            Placeholders.DEFAULT, 4,
            "vip", 6,
            "gold", 8,
            "admin", -1
        )),
        "Sets max. amount of merged chunks per claim based on player's rank/permissions.",
        "Use -1 for unlimited amount."
    );

    public static final ConfigValue<Long> HIGHLIGHT_CHUNK_UPDATE_RATE = ConfigValue.create("Highlighting.Chunk.UpdateRate",
        30L,
        "Sets update (redraw) rate (in ticks) for chunk claim bounds.",
        "Updates only when player went to other chunk and/or on player's Y location change.",
        "Setting this to low values may result in increased network usage.",
        "[Asynchronous]",
        "[Default is 30 (1.5 seconds)]"
    );

    public static final ConfigValue<Integer> HIGHLIGHT_CHUNK_RENDER_DISTANCE = ConfigValue.create("Highlighting.Chunk.RenderDistance",
        2,
        "Sets distance (in chunks) to render claim bounds.",
        "Setting this to high values may result in player's FPS drops.",
        "[Default is 2]"
    );

    public static final ConfigValue<Material> HIGHLIGHT_CHUNK_BLOCK_CORNER = ConfigValue.create("Highlighting.Chunk.CornerBlock",
        Material.class,
        Material.OAK_FENCE,
        "Block type used for a fake block display entity for chunk bound's corners.",
        "[Default is " + BukkitThing.toString(Material.OAK_FENCE) + "]"
    );

    public static final ConfigValue<Material> HIGHLIGHT_CHUNK_BLOCK_WIRE = ConfigValue.create("Highlighting.Chunk.WireBlock",
        Material.class,
        Material.AIR,
        "Block type used for a fake block display entity for chunk bound's corners connections.",
        "[Default is " + BukkitThing.toString(Material.AIR) + "]"
    );

    public static final ConfigValue<Material> HIGHLIGHT_REGION_BLOCK_CORNER = ConfigValue.create("Highlighting.Region.CornerBlock",
        Material.class,
        Material.WHITE_STAINED_GLASS,
        "Block type used for a fake block display entity for region selection's corners.",
        "[Default is " + BukkitThing.toString(Material.WHITE_STAINED_GLASS) + "]"
    );

    public static final ConfigValue<Material> HIGHLIGHT_REGION_BLOCK_WIRE = ConfigValue.create("Highlighting.Region.WireBlock",
        Material.class,
        Material.CHAIN,
        "Block type used for a fake block display entity for region selection's corners connections.",
        "[Default is " + BukkitThing.toString(Material.CHAIN) + "]"
    );

    public static int getDefaultPriority(@NotNull ClaimType type) {
        return (type == ClaimType.REGION ? REGION_DEFAULT_PRIORITY : LAND_DEFAULT_PRIORITY).get();
    }

    @NotNull
    public static ItemStack getDefaultIcon(@NotNull ClaimType type) {
        return (type == ClaimType.REGION ? REGION_DEFAULT_ICON : LAND_DEFAULT_ICON).get();
    }

    @NotNull
    public static Material getHighlightCorner(@NotNull HighlightType type) {
        if (type == HighlightType.CHUNK) return HIGHLIGHT_CHUNK_BLOCK_CORNER.get();

        return HIGHLIGHT_REGION_BLOCK_CORNER.get();
    }

    @NotNull
    public static Material getHighlightWire(@NotNull HighlightType type) {
        if (type == HighlightType.CHUNK) return HIGHLIGHT_CHUNK_BLOCK_WIRE.get();

        return HIGHLIGHT_REGION_BLOCK_WIRE.get();
    }

    @NotNull
    public static String getRegionAlias() {
        return REGION_COMMAND_ALIASES.get()[0];
    }

    @NotNull
    public static String getLandAlias() {
        return LAND_COMMAND_ALIASES.get()[0];
    }
}