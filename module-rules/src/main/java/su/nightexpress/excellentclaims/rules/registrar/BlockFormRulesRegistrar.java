package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.environment.IceFormRule;
import su.nightexpress.excellentclaims.rules.impl.environment.SnowFormRule;
import su.nightexpress.excellentclaims.rules.impl.environment.fade.CoralDieRule;
import su.nightexpress.excellentclaims.rules.impl.environment.fade.FarmlandDryRule;
import su.nightexpress.excellentclaims.rules.impl.environment.fade.FireBurnOutRule;
import su.nightexpress.excellentclaims.rules.impl.environment.fade.IceMeltRule;
import su.nightexpress.excellentclaims.rules.impl.environment.fade.SnowMeltRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

public final class BlockFormRulesRegistrar {

    private BlockFormRulesRegistrar() {
    }

    public static void register(RuleLoader loader, RulesSettings settings) {
        loader.addRuleSpec("snow_form", new SnowFormRule());
        loader.addRuleSpec("ice_form", new IceFormRule());

        loader.addRuleSpec("coral_die", new CoralDieRule());
        loader.addRuleSpec("farmland_dry", new FarmlandDryRule());
        loader.addRuleSpec("fire_burn_out", new FireBurnOutRule());
        loader.addRuleSpec("ice_melt", new IceMeltRule());
        loader.addRuleSpec("snow_melt", new SnowMeltRule());
    }
}
