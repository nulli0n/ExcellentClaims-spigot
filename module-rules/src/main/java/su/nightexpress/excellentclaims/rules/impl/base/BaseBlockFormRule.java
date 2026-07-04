package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NullMarked;

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
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getNewState().getLocation()))
            .shouldHandle(this::shouldHandle)
            .trigger((event, registry, claim, rule, allowed) -> {
                return RuleResult.of(allowed);
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockGrowEvent event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
