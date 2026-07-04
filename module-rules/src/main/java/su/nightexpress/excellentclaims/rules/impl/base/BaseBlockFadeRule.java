package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;
import org.jspecify.annotations.NullMarked;

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
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getBlock()))
            .shouldHandle(this::shouldHandle)
            .trigger((event, registry, claim, rule, allowed) -> {
                return RuleResult.of(allowed);
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockFadeEvent event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
