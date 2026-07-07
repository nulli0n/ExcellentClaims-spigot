package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.environment.block.fade.CoralDieRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.fade.FarmlandDryRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.fade.FireBurnOutRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.fade.IceMeltRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.fade.SnowMeltRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.form.IceFormRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.form.SnowFormRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class BlockFormRulesRegistrar {

    private BlockFormRulesRegistrar() {
    }

    public static void register(RuleLoader loader) {
        loader.addRuleSpec("snow_form", new SnowFormRule());
        loader.addRuleSpec("ice_form", new IceFormRule());

        loader.addRuleSpec("coral_die", new CoralDieRule());
        loader.addRuleSpec("farmland_dry", new FarmlandDryRule());
        loader.addRuleSpec("fire_burn_out", new FireBurnOutRule());
        loader.addRuleSpec("ice_melt", new IceMeltRule());
        loader.addRuleSpec("snow_melt", new SnowMeltRule());
    }
}
