package su.nightexpress.excellentclaims.engine.settings;

import java.util.Set;

import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

public final class EngineSettingsSchema {

    public static final ConfigProperty<Set<String>> DISABLED_CLAIM_MODULES = ConfigProperty.of(
        ConfigCodecs.STRING_SET,
        "Core.Disabled-Claim-Modules",
        Set.of(),
        "List here modules you want to disable completely."
    );

    public static final ConfigProperty<Integer> CLAIMS_AUTO_SAVE_INTERVAL = ConfigProperty.of(
        ConfigCodecs.INT,
        "Claim.Auto-Save.Interval",
        30,
        "Controls how often claim changes writes on disk."
    );

    private EngineSettingsSchema() {
    }
}
