package su.nightexpress.excellentclaims.engine.settings;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.core.settings.ConfigurableSettings;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class EngineSettings implements ConfigurableSettings {

    private Set<String> disabledClaimModules = Set.of();
    private int         autoSaveInterval;

    @Override
    public void loadFrom(FileConfig config) {
        this.disabledClaimModules = config.getOrSet(EngineSettingsSchema.DISABLED_CLAIM_MODULES);
        this.autoSaveInterval = config.getOrSet(EngineSettingsSchema.CLAIMS_AUTO_SAVE_INTERVAL);
    }

    public Set<String> getDisabledClaimModules() {
        return disabledClaimModules;
    }

    public int getAutoSaveInterval() {
        return autoSaveInterval;
    }
}
