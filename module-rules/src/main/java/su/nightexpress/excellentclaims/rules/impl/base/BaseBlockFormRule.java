package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockFormRule extends SimpleSpec<BlockFormEvent, Boolean> {

    public BaseBlockFormRule() {
        super(BlockFormEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockFormEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(this::shouldHandle)
            .process((event, registry, context) -> {
                Claim claim = registry.getPrioritizedClaim(event.getNewState().getLocation());
                Optional<Boolean> state = context.resolveValue(claim);
                return state.isEmpty() ? RuleResult.allow() : RuleResult.of(state.get());
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockGrowEvent event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
