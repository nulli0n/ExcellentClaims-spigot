package su.nightexpress.excellentclaims.rules.settings;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.Prefixed;
import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RulesSettings implements ConfigurableSettings, Prefixed {

    private String prefix = "";

    private boolean     allowHighFrequencyRules;
    private boolean     resetBlockAgeInBlockGrowEvent;
    private Set<String> defaultCommandBlacklist = Set.of();

    @Override
    public void loadFrom(FileConfig config) {
        this.prefix = config.getOrSet(RulesSettingsSchema.PREFIX);

        this.allowHighFrequencyRules = config.getOrSet(RulesSettingsSchema.ALLOW_HIGH_FREQUENCY_RULES);
        this.resetBlockAgeInBlockGrowEvent = config.getOrSet(RulesSettingsSchema.RULES_BLOCK_GROW_RESET_AGE);
        this.defaultCommandBlacklist = config.getOrSet(RulesSettingsSchema.RULES_COMMAND_USE_DEFAULT_BLACKLIST);
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    public boolean isAllowHighFrequencyRules() {
        return allowHighFrequencyRules;
    }

    public boolean isResetBlockAgeInBlockGrowEvent() {
        return resetBlockAgeInBlockGrowEvent;
    }

    public Set<String> getDefaultCommandBlacklist() {
        return defaultCommandBlacklist;
    }
}
