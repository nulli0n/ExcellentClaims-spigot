package su.nightexpress.excellentclaims.core.claim.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.core.claim.DefaultClaimRules;
import su.nightexpress.excellentclaims.core.rule.StoredRule;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class ClaimRulesCodec implements ConfigCodec<DefaultClaimRules> {

    public static final ClaimRulesCodec INSTANCE = new ClaimRulesCodec();

    @Override
    public DefaultClaimRules read(FileConfig config, String path) throws CodecReadException {
        Map<AdaptedKey, StoredRule<?>> properties = new HashMap<>();

        config.getSection(path).forEach(sId -> {
            StoredRule<?> value = config.get(path + "." + sId, StoredRule.class);
            if (value == null) return; // TODO Log

            properties.put(value.rule().key(), value);
        });

        return new DefaultClaimRules(properties);
    }

    @Override
    public void write(FileConfig config, String path, DefaultClaimRules properties) {
        config.remove(path);

        properties.getRuleMap().forEach((key, value) -> {
            config.set(path + "." + UUID.randomUUID(), value);
        });
    }
}
