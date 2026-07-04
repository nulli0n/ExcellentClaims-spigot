package su.nightexpress.excellentclaims.wilderness.settings;

import java.util.Arrays;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.Prefixed;
import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class WildernessSettings implements ConfigurableSettings, Prefixed {

    private String   prefix         = "";
    private String[] commandAliases = {};

    private String    wildernessDefaultName = "Wilderness";
    private NightItem wildernessDefaultIcon = NightItem.fromType(Material.GRASS_BLOCK);
    private int       wildernessDefaultPriority;

    private int wildernessNameMaxLength;
    private int wildernessDescriptionMaxLength;
    private int wildernessPriorityMinValue;
    private int wildernessPriorityMaxValue;

    private int uiSettingsIconSlot;

    @Override
    public void loadFrom(FileConfig config) {
        this.prefix = config.getOrSet(WildernessSettingsSchema.PREFIX);
        this.commandAliases = config.getOrSet(WildernessSettingsSchema.COMMAND_ALIASES);

        this.wildernessDefaultName = config.getOrSet(WildernessSettingsSchema.WILDERNESS_DEFAULT_NAME);
        this.wildernessDefaultIcon = config.getOrSet(WildernessSettingsSchema.WILDERNESS_DEFAULT_ICON);
        this.wildernessDefaultPriority = config.getOrSet(WildernessSettingsSchema.WILDERNESS_DEFAULT_PRIORITY);

        this.wildernessNameMaxLength = config.getOrSet(WildernessSettingsSchema.WILDERNESS_NAME_MAX_LENGTH);
        this.wildernessDescriptionMaxLength = config.getOrSet(
            WildernessSettingsSchema.WILDERNESS_DESCRIPTION_MAX_LENGTH);
        this.wildernessPriorityMinValue = config.getOrSet(WildernessSettingsSchema.WILDERNESS_PRIORITY_MIN_VALUE);
        this.wildernessPriorityMaxValue = config.getOrSet(WildernessSettingsSchema.WILDERNESS_PRIORITY_MAX_VALUE);

        this.uiSettingsIconSlot = config.getOrSet(WildernessSettingsSchema.UI_SETTINGS_ICON_SLOT);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    public String[] getCommandAliases() {
        return Arrays.copyOf(this.commandAliases, this.commandAliases.length);
    }

    public String getWildernessDefaultName() {
        return wildernessDefaultName;
    }

    public NightItem getDefaultIcon() {
        return this.wildernessDefaultIcon;
    }

    public int getDefaultPriority() {
        return this.wildernessDefaultPriority;
    }

    public int getWildernessNameMaxLength() {
        return wildernessNameMaxLength;
    }

    public int getWildernessDescriptionMaxLength() {
        return wildernessDescriptionMaxLength;
    }

    public int getWildernessPriorityMinValue() {
        return wildernessPriorityMinValue;
    }

    public int getWildernessPriorityMaxValue() {
        return wildernessPriorityMaxValue;
    }

    public int getUISettingsIconSlot() {
        return uiSettingsIconSlot;
    }
}
