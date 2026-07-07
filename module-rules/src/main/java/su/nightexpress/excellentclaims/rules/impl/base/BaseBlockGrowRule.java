package su.nightexpress.excellentclaims.rules.impl.base;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockGrowContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockGrowRule extends SimpleSpec<BlockGrowContext, Boolean> {

    protected final int weight;

    public BaseBlockGrowRule() {
        this(0);
    }

    public BaseBlockGrowRule(int weight) {
        super(BlockGrowContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
        this.weight = weight;
    }

    @Override
    public RuleBehavior<BlockGrowContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(this.weight)
            .shouldHandle(this::shouldHandle)
            .process((context, registry, resolver) -> {
                if (this.isAnyBlockDenied(registry, resolver, context.targetBlock(), context.sourceBlock())) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockGrowContext context);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}