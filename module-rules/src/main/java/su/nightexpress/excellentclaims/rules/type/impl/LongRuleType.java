package su.nightexpress.excellentclaims.rules.type.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class LongRuleType implements RuleType<Long> {

    public static final LongRuleType INSTANCE = new LongRuleType();

    @Override
    public String formatSummary(Long value) {
        return NumberUtil.format(value);
    }

    @Override
    public ConfigCodec<Long> getCodec() {
        return ConfigCodecs.LONG;
    }
}
