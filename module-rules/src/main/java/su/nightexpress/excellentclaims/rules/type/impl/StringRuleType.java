package su.nightexpress.excellentclaims.rules.type.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class StringRuleType implements RuleType<String> {

    public static final StringRuleType INSTANCE = new StringRuleType();

    @Override
    public String formatSummary(String value) {
        return value;
    }

    @Override
    public ConfigCodec<String> getCodec() {
        return ConfigCodecs.STRING;
    }
}
