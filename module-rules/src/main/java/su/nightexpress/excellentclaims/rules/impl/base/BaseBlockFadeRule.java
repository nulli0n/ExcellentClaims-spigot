package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockFadeRule extends SimpleSpec<BlockFadeEvent, Boolean> {

    public BaseBlockFadeRule() {
        super(BlockFadeEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockFadeEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(this::shouldHandle)
            .process((event, registry, context) -> {
                Claim claim = registry.getPrioritizedClaim(event.getBlock());
                Optional<Boolean> state = context.resolveValue(claim);
                return state.isEmpty() ? RuleResult.allow() : RuleResult.of(state.get());
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockFadeEvent event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
