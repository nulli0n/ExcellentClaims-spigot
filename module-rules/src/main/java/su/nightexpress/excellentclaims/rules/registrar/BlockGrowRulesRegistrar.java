package su.nightexpress.excellentclaims.rules.registrar;

import su.nightexpress.excellentclaims.rules.impl.environment.block.MushroomGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.TreeGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.AmethystGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.BambooGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.CactusGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.CaveVinesGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.CropGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.KelpGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.SugarCaneGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.TurtleEggHatchRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.TwistingVinesGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.VineGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.grow.WeepingVinesGrowRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.spread.AmethystSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.spread.FireSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.spread.GrassSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.spread.MyceliumSpreadRule;
import su.nightexpress.excellentclaims.rules.impl.environment.block.spread.VineSpreadRule;
import su.nightexpress.excellentclaims.rules.load.RuleLoader;

public final class BlockGrowRulesRegistrar {

    private BlockGrowRulesRegistrar() {
    }

    public static void register(RuleLoader loader) {
        loader.addRuleSpec("tree_grow", new TreeGrowRule());
        loader.addRuleSpec("mushroom_grow", new MushroomGrowRule());

        // Blocks that uses normal BlockGrowEvent
        loader.addRuleSpec("amethyst_grow", new AmethystGrowRule());
        loader.addRuleSpec("cactus_grow", new CactusGrowRule());
        loader.addRuleSpec("crop_grow", new CropGrowRule());
        loader.addRuleSpec("sugar_cane_grow", new SugarCaneGrowRule());
        loader.addRuleSpec("turtle_egg_hatch", new TurtleEggHatchRule());
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
