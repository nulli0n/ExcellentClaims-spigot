package su.nightexpress.excellentclaims.core.rule;

import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.nightcore.bridge.reflect.TypeReference;

public record StoredRule<T>(ClaimRule<T> rule, T value) {

    public static final TypeReference<StoredRule<?>> TYPE = new TypeReference<StoredRule<?>>() {

    };

}
