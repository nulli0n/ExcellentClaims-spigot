package su.nightexpress.excellentclaims.rules.impl.environment.block;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockExplodeContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class ExplosionDamageBlocksRule extends SimpleSpec<BlockExplodeContext, Boolean> {

    public ExplosionDamageBlocksRule() {
        super(BlockExplodeContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockExplodeContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(10)
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
        return RuleDefinition.builder("Explosion Destroy Blocks")
            .description(
                "Controls whether explosions can",
                "destroy blocks here.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.TNT)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
