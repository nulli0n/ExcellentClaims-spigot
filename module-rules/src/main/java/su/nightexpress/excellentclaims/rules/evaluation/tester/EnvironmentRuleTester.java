package su.nightexpress.excellentclaims.rules.evaluation.tester;

import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.rule.tester.EnvironmentTester;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockBurnContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockExplodeContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFadeContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFormContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFromToContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.evaluation.context.environment.StructureGrowContext;

@NullMarked
public class EnvironmentRuleTester implements EnvironmentTester {

    private final EvaluatorEngine engine;

    public EnvironmentRuleTester(EvaluatorEngine engine) {
        this.engine = engine;
    }

    @Override
    public boolean canBurn(Block block) {
        BlockBurnContext context = new BlockBurnContext(block);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canDecay(Block block, BlockState newState) {
        BlockFadeContext context = new BlockFadeContext(block, newState);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canExplode(Block block) {
        BlockExplodeContext context = new BlockExplodeContext(block);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canFlow(Block block, Block targetBlock) {
        BlockFromToContext context = new BlockFromToContext(block, targetBlock);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canForm(Block block, BlockState newState) {
        BlockFormContext context = new BlockFormContext(block, newState);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canGrow(Block block, Block targetBlock, BlockState newState) {
        BlockGrowContext context = new BlockGrowContext(block, targetBlock, newState);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canSpread(Block block, Block targetBlock, BlockState newState) {
        BlockSpreadContext context = new BlockSpreadContext(block, targetBlock, newState);
        return this.engine.quickTest(context);
    }

    @Override
    public boolean canTreeGrow(@Nullable Player actor, BlockState newState, TreeType species) {
        StructureGrowContext context = new StructureGrowContext(actor, newState, species);
        return this.engine.quickTest(context);
    }
}
