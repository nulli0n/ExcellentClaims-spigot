package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFromToContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockFromToRule extends SimpleSpec<BlockFromToContext, Boolean> {

    public BaseBlockFromToRule() {
        super(BlockFromToContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockFromToContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(this::shouldHandle)
            .process((context, registry, resolver) -> {
                Claim sourceClaim = registry.getPrioritizedClaim(context.sourceBlock());
                Claim targetClaim = registry.getPrioritizedClaim(context.targetBlock());

                if (sourceClaim != targetClaim && targetClaim != null) {
                    Optional<Boolean> state = resolver.resolveValue(targetClaim);
                    if (state.isPresent() && !state.get()) {
                        return RuleResult.deny();
                    }
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockFromToContext event);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
