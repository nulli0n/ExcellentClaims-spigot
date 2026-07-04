package su.nightexpress.excellentclaims.rules.impl.environment;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockGrowRule;

@NullMarked
public class VineGrowRule extends BaseBlockGrowRule {

    private static final Set<Material> VINES = Set.of(
        Material.VINE,
        Material.CAVE_VINES
    );

    public VineGrowRule() {
        super(false, BlockFace.SELF);
    }

    @Override
    protected boolean shouldHandle(BlockGrowEvent event) {
        return VINES.contains(event.getNewState().getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Vine Grow")
            .description("Controls whether vines can",
                "grow (on the same block) here."
            )
            .icon(Material.VINE)
            .build();
    }
}
