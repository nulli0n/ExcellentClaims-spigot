package su.nightexpress.excellentclaims.region.settings;

import java.util.Arrays;
import java.util.Set;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.Prefixed;
import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.integration.currency.CurrencyId;
import su.nightexpress.nightcore.util.RankTable;
import su.nightexpress.nightcore.util.RankTable.Mode;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RegionSettings implements ConfigurableSettings, Prefixed {

    private String   prefix         = "";
    private String[] commandAliases = {};

    private NightItem regionDefaultIcon = NightItem.fromType(Material.GRASS_BLOCK);
    private int       regionDefaultPriority;

    private int regionNameMaxLength;
    private int regionDescriptionMaxLength;
    private int regionPriorityMinValue;
    private int regionPriorityMaxValue;

    private Set<String> claimBannedWorlds      = Set.of();
    private RankTable   regionAmountQuota      = RankTable.builder(Mode.RANK, 0).build();
    private RankTable   regionSizeQuota        = RankTable.builder(Mode.RANK, 20_000).build();
    private boolean     region3DSizeValidation = true;

    private boolean billingEnabled;
    private String  billingCurrency = CurrencyId.VAULT;
    private double  billingCreationCost;

    private int uiSettingsIconSlot;

    private boolean     overlapEnabled;
    private Set<String> overlapAllowedTypes = Set.of();

    @Override
    public void loadFrom(FileConfig config) {
        this.prefix = config.getOrSet(RegionSettingsSchema.PREFIX);
        this.commandAliases = config.getOrSet(RegionSettingsSchema.COMMAND_ALIASES);

        this.regionDefaultIcon = config.getOrSet(RegionSettingsSchema.REGION_DEFAULT_ICON);
        this.regionDefaultPriority = config.getOrSet(RegionSettingsSchema.REGION_DEFAULT_PRIORITY);

        this.regionNameMaxLength = config.getOrSet(RegionSettingsSchema.REGION_NAME_MAX_LENGTH);
        this.regionDescriptionMaxLength = config.getOrSet(RegionSettingsSchema.REGION_DESCRIPTION_MAX_LENGTH);
        this.regionPriorityMinValue = config.getOrSet(RegionSettingsSchema.REGION_PRIORITY_MIN_VALUE);
        this.regionPriorityMaxValue = config.getOrSet(RegionSettingsSchema.REGION_PRIORITY_MAX_VALUE);

        this.claimBannedWorlds = config.getOrSet(RegionSettingsSchema.CLAIM_DISABLED_WORLDS);
        this.regionAmountQuota = config.getOrSet(RegionSettingsSchema.QUOTA_REGION_AMOUNT);
        this.regionSizeQuota = config.getOrSet(RegionSettingsSchema.QUOTA_REGION_SIZE);
        this.region3DSizeValidation = config.getOrSet(RegionSettingsSchema.QUOTA_SIZE_3D_VALIDATION);

        this.billingEnabled = config.getOrSet(RegionSettingsSchema.CLAIM_BILLING_ENABLED);
        this.billingCurrency = config.getOrSet(RegionSettingsSchema.CLAIM_BILLING_CURRENCY);
        this.billingCreationCost = config.getOrSet(RegionSettingsSchema.CLAIM_BILLING_CLAIM_COST);

        this.uiSettingsIconSlot = config.getOrSet(RegionSettingsSchema.UI_SETTINGS_ICON_SLOT);

        this.overlapEnabled = config.getOrSet(RegionSettingsSchema.OVERLAP_ENABLED);
        this.overlapAllowedTypes = config.getOrSet(RegionSettingsSchema.OVERLAP_ALLOWED_WITH);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    public String[] getCommandAliases() {
        return Arrays.copyOf(this.commandAliases, this.commandAliases.length);
    }

    public NightItem getDefaultIcon() {
        return this.regionDefaultIcon;
    }

    public int getDefaultPriority() {
        return this.regionDefaultPriority;
    }

    public int getRegionNameMaxLength() {
        return regionNameMaxLength;
    }

    public int getRegionDescriptionMaxLength() {
        return regionDescriptionMaxLength;
    }

    public int getRegionPriorityMinValue() {
        return regionPriorityMinValue;
    }

    public int getRegionPriorityMaxValue() {
        return regionPriorityMaxValue;
    }

    public Set<String> getClaimBannedWorlds() {
        return this.claimBannedWorlds;
    }


    public RankTable getRegionAmountQuota() {
        return regionAmountQuota;
    }

    public RankTable getRegionSizeQuota() {
        return regionSizeQuota;
    }

    public boolean isRegion3DSizeValidation() {
        return region3DSizeValidation;
    }


    public boolean isBillingEnabled() {
        return billingEnabled;
    }

    public String getBillingCurrency() {
        return billingCurrency;
    }

    public double getBillingCreationCost() {
        return billingCreationCost;
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
