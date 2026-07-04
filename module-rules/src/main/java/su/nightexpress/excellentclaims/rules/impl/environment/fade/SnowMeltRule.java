package su.nightexpress.excellentclaims.rules.impl.environment.fade;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class SnowMeltRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeEvent event) {
        Material type = event.getBlock().getType();
        return type == Material.SNOW; // Only layers can smelt
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Snow Melt")
            .description(
                "Controls whether snow can",
                "melt here."
            )
            .icon(Material.SNOW_BLOCK)
            .build();
    }
}
