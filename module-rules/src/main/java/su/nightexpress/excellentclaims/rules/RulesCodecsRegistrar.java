package su.nightexpress.excellentclaims.rules;

import su.nightexpress.excellentclaims.api.core.DependencyContainer;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResolver;
import su.nightexpress.excellentclaims.core.rule.StoredRule;
import su.nightexpress.excellentclaims.rules.codec.RuleDefinitionCodec;
import su.nightexpress.excellentclaims.rules.codec.StoredRuleCodec;
import su.nightexpress.excellentclaims.rules.filter.codec.StandardFilterTypes;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

public final class RulesCodecsRegistrar {

    private RulesCodecsRegistrar() {
    }

    public static void register(DependencyContainer container) {
        RuleResolver resolver = container.get(RuleResolver.class);

        ConfigCodecs.register(RuleDefinition.class, RuleDefinitionCodec.INSTANCE);
        ConfigCodecs.register(StoredRule.TYPE, new StoredRuleCodec(resolver));

        ConfigCodecs.register(StandardFilterTypes.MATERIAL, RuleTypes.MATERIALS.getCodec());
        ConfigCodecs.register(StandardFilterTypes.ENTITY_TYPE, RuleTypes.ENTITY_TYPES.getCodec());
        ConfigCodecs.register(StandardFilterTypes.DAMAGE_TYPE, RuleTypes.DAMAGE_TYPES.getCodec());
        ConfigCodecs.register(StandardFilterTypes.COMMAND, RuleTypes.COMMANDS.getCodec());
    }
}
