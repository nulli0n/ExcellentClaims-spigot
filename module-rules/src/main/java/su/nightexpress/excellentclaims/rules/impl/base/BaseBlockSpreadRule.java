package su.nightexpress.excellentclaims.rules.impl.base;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockSpreadContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockSpreadRule extends SimpleSpec<BlockSpreadContext, Boolean> {

    public BaseBlockSpreadRule() {
        super(BlockSpreadContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockSpreadContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(this::shouldHandle)
            .process((context, registry, resolver) -> {
                if (this.isAnyBlockDenied(registry, resolver, context.targetBlock(), context.sourceBlock())) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockSpreadContext event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
