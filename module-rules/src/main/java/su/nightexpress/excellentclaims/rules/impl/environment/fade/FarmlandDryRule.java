package su.nightexpress.excellentclaims.rules.impl.environment.fade;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class FarmlandDryRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeEvent event) {
        Material type = event.getBlock().getType();
        return type == Material.FARMLAND;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Farmland Dry")
            .description(
                "Controls whether farmlands can",
                "dry naturally here."
            )
            .icon(Material.FARMLAND)
            .build();
    }
}
