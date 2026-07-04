package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.environment.AmethystGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.AmethystSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.BambooGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.CactusGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.CaveVinesGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.CropGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.FireSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.GrassSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.KelpGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.MushroomGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.MyceliumSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.SugarCaneGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.TreeGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.TurtleEggHatchRule;
import su.nightexpress.excellentclaims.rules.impl.environment.TwistingVinesGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.VineGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.VineSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.WeepingVinesGrowRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;
import su.nightexpress.excellentclaims.rules.settings.RulesSettings;

public final class BlockGrowRulesRegistrar {

    private BlockGrowRulesRegistrar() {
    }

    public static void register(RuleLoader loader, RulesSettings settings) {
        boolean resetBlockAge = settings.isResetBlockAgeInBlockGrowEvent();

        loader.addRuleSpec("tree_grow", new TreeGrowRule());
        loader.addRuleSpec("mushroom_grow", new MushroomGrowRule());

        // Blocks that uses normal BlockGrowEvent
        loader.addRuleSpec("amethyst_grow", new AmethystGrowRule(resetBlockAge));
        loader.addRuleSpec("cactus_grow", new CactusGrowRule(resetBlockAge));
        loader.addRuleSpec("crop_grow", new CropGrowRule(resetBlockAge));
        loader.addRuleSpec("sugar_cane_grow", new SugarCaneGrowRule(resetBlockAge));
        loader.addRuleSpec("turtle_egg_hatch", new TurtleEggHatchRule(resetBlockAge));
        loader.addRuleSpec("vine_grow", new VineGrowRule());

        // Blocks that uses BlockSpreadEvent while growing like normal BlockGrowEvent lol
        loader.addRuleSpec("bamboo_grow", new BambooGrowRule());
        loader.addRuleSpec("cave_vines_grow", new CaveVinesGrowRule());
        loader.addRuleSpec("kelp_grow", new KelpGrowRule());
        loader.addRuleSpec("twisting_vines_grow", new TwistingVinesGrowRule());
        loader.addRuleSpec("weeping_vines_grow", new WeepingVinesGrowRule());

        // Blocks that uses BlockSpreadEvent as expected
        loader.addRuleSpec("amethyst_spread", new AmethystSpreadRule());
        loader.addRuleSpec("fire_spread", new FireSpreadRule());
        loader.addRuleSpec("grass_spread", new GrassSpreadRule());
        loader.addRuleSpec("mycelium_spread", new MyceliumSpreadRule());
        loader.addRuleSpec("vine_spread", new VineSpreadRule());
    }
}
