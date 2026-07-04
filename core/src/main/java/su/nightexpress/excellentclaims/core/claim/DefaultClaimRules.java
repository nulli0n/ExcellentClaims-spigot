package su.nightexpress.excellentclaims.core.claim;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.core.rule.StoredRule;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class DefaultClaimRules implements ClaimRules {

    private final Map<AdaptedKey, StoredRule<?>> ruleMap;

    public DefaultClaimRules() {
        this(Map.of());
    }

    public DefaultClaimRules(Map<AdaptedKey, StoredRule<?>> properties) {
        this.ruleMap = new HashMap<>(properties);
    }

    public Map<AdaptedKey, StoredRule<?>> getRuleMap() {
        return Map.copyOf(this.ruleMap);
    }

    public <T> T getOrDefault(ClaimRule<T> property) {
        return this.get(property, property.getDefaultValue());
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ClaimRule<T> property, T defaultValue) {
        if (!this.has(property)) return defaultValue;

        StoredRule<T> value = (StoredRule<T>) this.ruleMap.get(property.key());
        return value.value();
    }

    public <T> boolean has(ClaimRule<T> property) {
        return this.ruleMap.containsKey(property.key());
    }

    public <T> void set(ClaimRule<T> property, T value) {
        this.ruleMap.put(property.key(), new StoredRule<>(property, value));
    }

    public <T> void unset(ClaimRule<T> property) {
        this.ruleMap.remove(property.key());
    }
}