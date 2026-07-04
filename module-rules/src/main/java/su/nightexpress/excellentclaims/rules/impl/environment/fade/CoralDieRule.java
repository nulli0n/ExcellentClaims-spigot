package su.nightexpress.excellentclaims.rules.impl.environment.fade;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFadeRule;

@NullMarked
public class CoralDieRule extends BaseBlockFadeRule {

    @Override
    protected boolean shouldHandle(BlockFadeEvent event) {
        Material type = event.getNewState().getType();
        return Tag.CORALS.isTagged(type) || Tag.CORAL_BLOCKS.isTagged(type);
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Coral Die")
            .description(
                "Controls whether corals can",
                "die here."
            )
            .icon(Material.BRAIN_CORAL)
            .build();
    }
}
