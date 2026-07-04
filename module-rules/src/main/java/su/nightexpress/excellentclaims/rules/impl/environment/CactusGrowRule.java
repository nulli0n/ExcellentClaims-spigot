package su.nightexpress.excellentclaims.rules.impl.environment;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class CactusGrowRule extends BaseBlockGrowRule {

    private static final Set<Material> CACTUSES = Set.of(
        Material.CACTUS,
        Material.CACTUS_FLOWER
    );

    public CactusGrowRule(boolean resetBlockAge) {
        super(resetBlockAge, BlockFace.UP);
    }

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return CACTUSES.contains(event.getNewState().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Cactus Grow")
            .description("Controls whether cactus can",
                "grow here."
            )
            .icon(Material.CACTUS)
            .build();
    }
}
