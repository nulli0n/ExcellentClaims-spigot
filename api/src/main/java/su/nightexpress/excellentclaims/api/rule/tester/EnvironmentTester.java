package su.nightexpress.excellentclaims.api.rule.tester;

import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface EnvironmentTester {

    boolean canBurn(Block block);

    boolean canDecay(Block block, BlockState newState);

    boolean canExplode(Block block);

    boolean canFlow(Block block, Block targetBlock);

    boolean canForm(Block block, BlockState newState);

    boolean canGrow(Block block, Block targetBlock, BlockState newState);

    boolean canSpread(Block block, Block targetBlock, BlockState newState);

    boolean canTreeGrow(@Nullable Player actor, BlockState newState, TreeType species);
}
