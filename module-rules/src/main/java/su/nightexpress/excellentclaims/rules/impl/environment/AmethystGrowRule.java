package su.nightexpress.excellentclaims.rules.impl.environment;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;
import su.nightexpress.nightcore.util.Lists;

@NullMarked
public class AmethystGrowRule extends BaseBlockGrowRule {

    private static final Set<Material> AMETHYSTS = Lists.newSet(
        Material.SMALL_AMETHYST_BUD,
        Material.MEDIUM_AMETHYST_BUD,
        Material.LARGE_AMETHYST_BUD,
        Material.AMETHYST_CLUSTER
    );

    public AmethystGrowRule(boolean resetBlockAge) {
        super(resetBlockAge, BlockFace.SELF);
    }

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return AMETHYSTS.contains(event.getNewState().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Amethyst Grow")
            .description("Controls whether amethysts can",
                "grow here."
            )
            .icon(Material.MEDIUM_AMETHYST_BUD)
            .build();
    }
}
