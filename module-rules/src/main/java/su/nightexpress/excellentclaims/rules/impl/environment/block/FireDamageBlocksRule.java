package su.nightexpress.excellentclaims.rules.impl.environment.block;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockBurnContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class FireDamageBlocksRule extends SimpleSpec<BlockBurnContext, Boolean> {

    public FireDamageBlocksRule() {
        super(BlockBurnContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockBurnContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(event -> true)
            .process((context, registry, resolver) -> {
                if (this.isAnyBlockDenied(registry, resolver, context.block())) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Fire Damage Blocks")
            .description("Controls whether fire can",
                "destroy blocks."
            )
            .icon(Material.FLINT_AND_STEEL)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
