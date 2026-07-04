package su.nightexpress.excellentclaims.land.settings;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.Prefixed;
import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.RankTable;
import su.nightexpress.nightcore.util.RankTable.Mode;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class LandSettings implements ConfigurableSettings, Prefixed {

    private String   prefix         = "";
    private String[] commandAliases = {};

    private String    landDefaultName = "";
    private NightItem landDefaultIcon = NightItem.fromType(Material.GRASS_BLOCK);
    private int       landDefaultPriority;

    private int landNameMaxLength;
    private int landDescriptionMaxLength;
    private int landPriorityMinValue;
    private int landPriorityMaxValue;

    private Set<String> claimBannedWorlds    = Set.of();
    private RankTable   maxLandAmountPerRank = RankTable.builder(Mode.RANK, 0).build();
    private RankTable   maxLandChunksPerRank = RankTable.builder(Mode.RANK, 0).build();

    private boolean billingEnabled;
    private String  billingCurrency = CurrencyId.VAULT;
    private double  billingChunkClaimCost;

    private int uiSettingsIconSlot;

    private boolean     overlapEnabled;
    private Set<String> overlapAllowedTypes = Set.of();

    @Override
    public void loadFrom(FileConfig config) {
        this.prefix = config.getOrSet(LandSettingsSchema.PREFIX);
        this.commandAliases = config.getOrSet(LandSettingsSchema.COMMAND_ALIASES);

        this.landDefaultName = config.getOrSet(LandSettingsSchema.LAND_DEFAULT_NAME);
        this.landDefaultIcon = config.getOrSet(LandSettingsSchema.LAND_DEFAULT_ICON);
        this.landDefaultPriority = config.getOrSet(LandSettingsSchema.LAND_DEFAULT_PRIORITY);

        this.landNameMaxLength = config.getOrSet(LandSettingsSchema.LAND_NAME_MAX_LENGTH);
        this.landDescriptionMaxLength = config.getOrSet(LandSettingsSchema.LAND_DESCRIPTION_MAX_LENGTH);
        this.landPriorityMinValue = config.getOrSet(LandSettingsSchema.LAND_PRIORITY_MIN_VALUE);
        this.landPriorityMaxValue = config.getOrSet(LandSettingsSchema.LAND_PRIORITY_MAX_VALUE);

        this.claimBannedWorlds = config.getOrSet(LandSettingsSchema.CLAIM_DISABLED_WORLDS);
        this.maxLandAmountPerRank = config.getOrSet(LandSettingsSchema.LIMITS_MAX_LANDS_PER_PLAYER);
        this.maxLandChunksPerRank = config.getOrSet(LandSettingsSchema.LIMITS_MAX_CHUNKS_PER_LAND);

        this.billingEnabled = config.getOrSet(LandSettingsSchema.CLAIM_BILLING_ENABLED);
        this.billingCurrency = config.getOrSet(LandSettingsSchema.CLAIM_BILLING_CURRENCY);
        this.billingChunkClaimCost = config.getOrSet(LandSettingsSchema.CLAIM_BILLING_CLAIM_COST);

        this.uiSettingsIconSlot = config.getOrSet(LandSettingsSchema.UI_SETTINGS_ICON_SLOT);

        this.overlapEnabled = config.getOrSet(LandSettingsSchema.OVERLAP_ENABLED);
        this.overlapAllowedTypes = config.getOrSet(LandSettingsSchema.OVERLAP_ALLOWED_WITH);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    public String[] getCommandAliases() {
        return Arrays.copyOf(this.commandAliases, this.commandAliases.length);
    }

    public String getDefaultName() {
        return this.landDefaultName;
    }

    public NightItem getDefaultIcon() {
        return this.landDefaultIcon;
    }

    public int getDefaultPriority() {
        return this.landDefaultPriority;
    }

    public int getLandNameMaxLength() {
        return landNameMaxLength;
    }

    public int getLandDescriptionMaxLength() {
        return landDescriptionMaxLength;
    }

    public int getLandPriorityMinValue() {
        return landPriorityMinValue;
    }

    public int getLandPriorityMaxValue() {
        return landPriorityMaxValue;
    }

    public Set<String> getClaimBannedWorlds() {
        return this.claimBannedWorlds;
    }

    public int getMaxClaims(Player player) {
        return this.maxLandAmountPerRank.getGreatestOrNegative(player).intValue();
    }

    public int getMaxClaimSize(Player player) {
        return this.maxLandChunksPerRank.getGreatestOrNegative(player).intValue();
    }


    public boolean isBillingEnabled() {
        return billingEnabled;
    }

    public String getBillingCurrency() {
        return billingCurrency;
    }

    public double getBillingChunkClaimCost() {
        return billingChunkClaimCost;
    }


    public int getUISettingsIconSlot() {
        return uiSettingsIconSlot;
    }

    public boolean isOverlappingAllowed() {
        return this.overlapEnabled;
    }

    public Set<String> getAllowedOverlappingTypes() {
        return this.overlapAllowedTypes;
    }
}
