package su.nightexpress.excellentclaims.region.settings;

import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.RankTable;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public final class RegionSettingsSchema {

    private static final String DEFAULT_PREFIX = TagWrappers.GRADIENT_3.with("#fdb813", "#ffd700", "#f8d47a")
        .and(TagWrappers.BOLD)
        .wrap("REGIONS") + TagWrappers.DARK_GRAY.wrap(" » ");

    public static final ConfigProperty<String> PREFIX = ConfigProperty.of(ConfigCodecs.STRING,
        "Core.Prefix",
        DEFAULT_PREFIX,
        "Prefix for all region-related chat messages."
    );

    public static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(ConfigCodecs.STRING_ARRAY,
        "Core.Command-Aliases",
        new String[]{"region", "regions", "rg"},
        "Command aliases for the Regions module."
    );

    public static final ConfigProperty<NightItem> REGION_DEFAULT_ICON = ConfigProperty.of(ConfigCodecs.NIGHT_ITEM,
        "Region.Default-Icon",
        NightItem.fromType(Material.GRASS_BLOCK),
        "Default icon for new regions."
    );

    public static final ConfigProperty<Integer> REGION_DEFAULT_PRIORITY = ConfigProperty.of(ConfigCodecs.INT,
        "Region.Default-Priority",
        2,
        "Default priority value for new regions.",
        "[Default is 2]"
    );

    public static final ConfigProperty<Integer> REGION_NAME_MAX_LENGTH = ConfigProperty.of(ConfigCodecs.INT,
        "Region.Name.Max-Length",
        24,
        "Sets max. allowed length for region names.",
        "Color & decoration tags do not count."
    );

    public static final ConfigProperty<Integer> REGION_DESCRIPTION_MAX_LENGTH = ConfigProperty.of(ConfigCodecs.INT,
        "Region.Description.Max-Length",
        48,
        "Sets max. allowed length for region descriptions.",
        "Color & decoration tags do not count."
    );

    public static final ConfigProperty<Integer> REGION_PRIORITY_MIN_VALUE = ConfigProperty.of(ConfigCodecs.INT,
        "Region.Priority.Min-Value",
        0,
        "Set min. allowed priority value for regions."
    );

    public static final ConfigProperty<Integer> REGION_PRIORITY_MAX_VALUE = ConfigProperty.of(ConfigCodecs.INT,
        "Region.Priority.Max-Value",
        10,
        "Set max. allowed priority value for regions."
    );

    public static final ConfigProperty<Set<String>> CLAIM_DISABLED_WORLDS = ConfigProperty.of(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "Claiming.Disabled-Worlds",
        Set.of("minecraft:custom_nether", "someplugin:some_world"),
        "List of worlds, where players can not create regions."
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
        "Sets cost for claiming regions."
    );

    public static final ConfigProperty<RankTable> QUOTA_REGION_AMOUNT = ConfigProperty.of(
        ConfigCodecs.RANK_TABLE,
        "Quota.Max-Regions-Per-Player",
        RankTable.builder(RankTable.Mode.RANK, 4)
            .permissionPrefix("regions.amount.")
            .addRankValue("vip", 5)
            .addRankValue("premium", 7)
            .addRankValue("owner", -1)
            .build(),
        "Controls how much regions a player can create."
    );

    public static final ConfigProperty<RankTable> QUOTA_REGION_SIZE = ConfigProperty.of(
        ConfigCodecs.RANK_TABLE,
        "Quota.Max-Blocks-Per-Player",
        RankTable.builder(RankTable.Mode.RANK, 20_000)
            .permissionPrefix("regions.size.")
            .addRankValue("vip", 50_000)
            .addRankValue("premium", 70_000)
            .addRankValue("owner", -1)
            .build(),
        "Sets max region size (in blocks) for players."
    );

    public static final ConfigProperty<Boolean> QUOTA_SIZE_3D_VALIDATION = ConfigProperty.of(
        ConfigCodecs.BOOLEAN,
        "Quota.Region-Size-3D-Validation",
        true,
        "Whether region size caluclates in 3-dimensions instead of 2D."
    );

    public static final ConfigProperty<Integer> UI_SETTINGS_ICON_SLOT = ConfigProperty.of(ConfigCodecs.INT,
        "UI.Settings.Icon-Slot",
        13,
        "Sets slot for the 'Icon' button in Region GUI."
    );

    public static final ConfigProperty<Boolean> OVERLAP_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
        "Overlap.Enabled",
        false,
        "Controls whether regions can overlap with other claim types (e.g. Land Claims)."
    );

    public static final ConfigProperty<Set<String>> OVERLAP_ALLOWED_WITH = ConfigProperty.of(
        ConfigCodecs.STRING_SET_LOWER_CASE,
        "Overlap.Allowed-With-Types",
        Set.of("regions"),
        "List of claim types with which regions can overlap with."
    );

    private RegionSettingsSchema() {
    }
}
