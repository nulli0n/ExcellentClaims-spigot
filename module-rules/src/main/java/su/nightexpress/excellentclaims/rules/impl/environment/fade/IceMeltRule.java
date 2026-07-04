package su.nightexpress.excellentclaims.rules.impl.environment.fade;

import org.bukkit.Material;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class IceMeltRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeEvent event) {
        Material type = event.getBlock().getType();
        return type == Material.ICE || type == Material.FROSTED_ICE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Ice Melt")
            .description(
                "Controls whether ice can",
                "melt here."
            )
            .icon(Material.ICE)
            .build();
    }
}
