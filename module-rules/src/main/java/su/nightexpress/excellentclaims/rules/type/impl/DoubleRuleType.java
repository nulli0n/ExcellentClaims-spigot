package su.nightexpress.excellentclaims.rules.type.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class DoubleRuleType implements RuleType<Double> {

    public static final DoubleRuleType INSTANCE = new DoubleRuleType();

    @Override
    public String formatSummary(Double value) {
        return NumberUtil.format(value);
    }

    @Override
    public ConfigCodec<Double> getCodec() {
        return ConfigCodecs.DOUBLE;
    }
}
