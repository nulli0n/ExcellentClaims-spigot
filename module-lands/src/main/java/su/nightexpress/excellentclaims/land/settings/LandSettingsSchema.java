package su.nightexpress.excellentclaims.land.settings;

import java.util.Set;

import org.bukkit.Material;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.RankTable;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public final class LandSettingsSchema {

    private static final String DEFAULT_PREFIX = TagWrappers.GRADIENT.with("#4caf50", "#8bc34a")
        .and(TagWrappers.BOLD)
        .wrap("CLAIMS") + TagWrappers.DARK_GRAY.wrap(" » ");

    public static final ConfigProperty<String> PREFIX = ConfigProperty.of(ConfigCodecs.STRING,
        "Core.Prefix",
        DEFAULT_PREFIX,
        "Prefix for all Land claim-related chat messages."
    );

    public static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(ConfigCodecs.STRING_ARRAY,
        "Core.Command-Aliases",
        new String[]{"land", "lands", "l"},
        "Command aliases for the Lands module."
    );

    public static final ConfigProperty<String> LAND_DEFAULT_NAME = ConfigProperty.of(ConfigCodecs.STRING,
        "Land.Default-Name",
        CommonPlaceholders.PLAYER_NAME + "'s Land",
        "Default name for new Land claims if not specified by a player upon creation.",
        "Use '%s' placeholder for a player name.".formatted(CommonPlaceholders.PLAYER_NAME)
    );

    public static final ConfigProperty<NightItem> LAND_DEFAULT_ICON = ConfigProperty.of(ConfigCodecs.NIGHT_ITEM,
        "Land.Default-Icon",
        NightItem.fromType(Material.GRASS_BLOCK),
        "Default icon for new Land claims."
    );

    public static final ConfigProperty<Integer> LAND_DEFAULT_PRIORITY = ConfigProperty.of(ConfigCodecs.INT,
        "Land.Default-Priority",
        5,
        "Default priority value for new Land claims.",
        "Claims/Regions with highest priority overrides lowest ones.",
        "Might be useful if you're using mixed system with Regions and/or Wilderness.",
        "[Default is 10]"
    );

    public static final ConfigProperty<Integer> LAND_NAME_MAX_LENGTH = ConfigProperty.of(ConfigCodecs.INT,
        "Land.Name.Max-Length",
        24,
        "Sets max. allowed length for Land claim names.",
        "Color & decoration tags do not count."
    );

    public static final ConfigProperty<Integer> LAND_DESCRIPTION_MAX_LENGTH = ConfigProperty.of(ConfigCodecs.INT,
        "Land.Description.Max-Length",
        48,
        "Sets max. allowed length for Land claim descriptions.",
        "Color & decoration tags do not count."
    );

    public static final ConfigProperty<Integer> LAND_PRIORITY_MIN_VALUE = ConfigProperty.of(ConfigCodecs.INT,
        "Land.Priority.Min-Value",
        0,
        "Set min. allowed priority value for Land claims.",
        "Might be useful if you're using mixed system with Regions and/or Wilderness."
    );

    public static final ConfigProperty<Integer> LAND_PRIORITY_MAX_VALUE = ConfigProperty.of(ConfigCodecs.INT,
        "Land.Priority.Max-Value",
        10,
        "Set max. allowed priority value for Land claims.",
        "Might be useful if you're using mixed system with Regions and/or Wilderness."
    );

    public static final ConfigProperty<Set<String>> CLAIM_DISABLED_WORLDS = ConfigProperty.of(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "Claiming.Disabled-Worlds",
        Set.of("minecraft:custom_nether", "someplugin:some_world"),
        "List of worlds, where players can not claim chunks for their Land claims."
    );

    public static final ConfigProperty<Boolean> CLAIM_BILLING_ENABLED = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "Claiming.Billing.Enabled",
        false,
        "Controls whether Billing feature is enabled."
    );

    public static final ConfigProperty<String> CLAIM_BILLING_CURRENCY = ConfigProperty.of(
        ConfigCodecs.STRING,
        "Claiming.Billing.Currency",
        CurrencyId.VAULT,
        "Sets currency for the Billing feature."
    );

    public static final ConfigProperty<Double> CLAIM_BILLING_CLAIM_COST = ConfigProperty.of(
        ConfigCodecs.DOUBLE,
        "Claiming.Billing.Chunk-Claim-Cost",
        500D,
        "Sets cost for claiming chunks into Land claims."
    );

    public static final ConfigProperty<RankTable> LIMITS_MAX_LANDS_PER_PLAYER = ConfigProperty.of(
        ConfigCodecs.RANK_TABLE,
        "Limits.Max-Lands-Per-Player",
        RankTable.builder(RankTable.Mode.RANK, 4)
            .permissionPrefix("lands.amount.")
            .addRankValue("vip", 5)
            .addRankValue("premium", 7)
            .addRankValue("owner", -1)
            .build(),
        "Controls how much Land claims a player can create."
    );

    public static final ConfigProperty<RankTable> LIMITS_MAX_CHUNKS_PER_LAND = ConfigProperty.of(
        ConfigCodecs.RANK_TABLE,
        "Limits.Max-Chunks-Per-Land",
        RankTable.builder(RankTable.Mode.RANK, 2)
            .permissionPrefix("lands.size.")
            .addRankValue("vip", 4)
            .addRankValue("premium", 6)
            .addRankValue("owner", -1)
            .build(),
        "Sets max. amount of chunks per land claim based on player's rank/permissions.",
        "Use -1 for unlimited amount."
    );

    public static final ConfigProperty<Integer> UI_SETTINGS_ICON_SLOT = ConfigProperty.of(ConfigCodecs.INT,
        "UI.Settings.Icon-Slot",
        13,
        "Sets slot for the 'Icon' button in Claim GUI."
    );

    public static final ConfigProperty<Boolean> OVERLAP_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
        "Land.Overlap.Enabled",
        false,
        "Controls whether Land claims can overlap with other claim types (e.g. Regions)."
    );

    public static final ConfigProperty<Set<String>> OVERLAP_ALLOWED_WITH = ConfigProperty.of(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "Land.Overlap.Allowed-With-Types",
        Set.of("regions"),
        "List of claim types with which Land claims can overlap."
    );

    private LandSettingsSchema() {
    }
}
