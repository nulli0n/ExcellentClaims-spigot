package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockFadeContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockFadeRule extends SimpleSpec<BlockFadeContext, Boolean> {

    public BaseBlockFadeRule() {
        super(BlockFadeContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockFadeContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(this::shouldHandle)
            .process((context, registry, resolver) -> {
                Claim claim = registry.getPrioritizedClaim(context.block());
                Optional<Boolean> state = resolver.resolveValue(claim);
                return state.isEmpty() ? RuleResult.allow() : RuleResult.of(state.get());
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockFadeContext context);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
