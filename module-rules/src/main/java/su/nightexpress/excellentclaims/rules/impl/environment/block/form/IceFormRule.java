package su.nightexpress.excellentclaims.rules.impl.environment.block.form;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFormContext;
import su.nightexpress.excellentclaims.rules.impl.base.BaseBlockFormRule;

@NullMarked
public class IceFormRule extends BaseBlockFormRule {

    @Override
    protected boolean shouldHandle(BlockFormContext context) {
        return context.newState().getType() == Material.ICE;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Ice Form")
            .description("Controls whether ice can",
                "form here."
            )
            .icon(Material.ICE)
            .build();
    }
}
