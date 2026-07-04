package su.nightexpress.excellentclaims.rules.type.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class IntRuleType implements RuleType<Integer> {

    public static final IntRuleType INSTANCE = new IntRuleType();

    @Override
    public String formatSummary(Integer value) {
        return NumberUtil.format(value);
    }

    @Override
    public ConfigCodec<Integer> getCodec() {
        return ConfigCodecs.INT;
    }
}
