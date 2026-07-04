package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.environment.LavaFlowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.WaterFlowRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

public final class HighFrequencyRulesRegistrar {

    private HighFrequencyRulesRegistrar() {
    }

    public static void register(RuleLoader loader, RulesSettings settings) {
        loader.addRuleSpec("lava_flow", new LavaFlowRule());
        loader.addRuleSpec("water_flow", new WaterFlowRule());
    }
}
