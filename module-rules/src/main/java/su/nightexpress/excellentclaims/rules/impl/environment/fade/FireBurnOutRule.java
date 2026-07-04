package su.nightexpress.excellentclaims.rules.impl.environment.fade;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class FireBurnOutRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeEvent event) {
        Material type = event.getNewState().getType();
        return type == Material.FIRE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Fire Burn Out")
            .description(
                "Controls whether fire can",
                "burn out here."
            )
            .icon(Material.FLINT_AND_STEEL)
            .build();
    }
}
