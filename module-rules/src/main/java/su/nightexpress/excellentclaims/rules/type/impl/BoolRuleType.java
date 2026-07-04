package su.nightexpress.excellentclaims.rules.type.impl;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class BoolRuleType implements RuleType<Boolean> {

    public static final BoolRuleType INSTANCE = new BoolRuleType();

    @Override
    public String formatSummary(Boolean value) {
        return CoreLang.STATE_ENABLED_DISALBED.get(value);
    }

    @Override
    public ConfigCodec<Boolean> getCodec() {
        return ConfigCodecs.BOOLEAN;
    }

}
