package su.nightexpress.excellentclaims.rules.codec;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleResolver;
import su.nightexpress.excellentclaims.core.rule.StoredRule;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class StoredRuleCodec implements ConfigCodec<StoredRule<?>> {

    private final RuleResolver resolver;

    public StoredRuleCodec(RuleResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public StoredRule<?> read(FileConfig config, String path) throws CodecReadException {
        String ruleId = config.get(path + ".rule", ConfigCodecs.STRING);
        if (ruleId == null) throw new CodecReadException("No rule key found");

        ClaimRule<?> rule = this.resolver.resolve(ruleId);
        if (rule == null) {
            throw new CodecReadException("Unknown rule: '" + ruleId + "'");
        }

        StoredRule<?> value = this.loadRule(rule, config, path);
        if (value == null) {
            throw new CodecReadException("No value present for '" + ruleId + "' rule");
        }

        return value;
    }

    @Override
    public void write(FileConfig config, String path, StoredRule<?> value) {
        this.writeRule(value, config, path);
    }

    private <T> @Nullable StoredRule<T> loadRule(ClaimRule<T> rule, FileConfig config, String path) {
        T value = config.get(path + ".value", rule.getType().getCodec());
        if (value == null) return null;

        return new StoredRule<T>(rule, value);
    }

    private <T> void writeRule(StoredRule<T> packed, FileConfig config, String path) {
        ClaimRule<T> rule = packed.rule();
        T value = packed.value();

        config.set(path + ".rule", rule.key().asString());
        config.set(path + ".value", rule.getType().getCodec(), value);
    }
}
