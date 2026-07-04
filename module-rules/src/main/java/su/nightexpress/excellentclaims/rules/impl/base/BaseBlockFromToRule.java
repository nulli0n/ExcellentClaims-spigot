package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFromToEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockFromToRule extends SimpleSpec<BlockFromToEvent, Boolean> {

    public BaseBlockFromToRule() {
        super(BlockFromToEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockFromToEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getToBlock()))
            .shouldHandle(this::shouldHandle)
            .trigger((event, registry, targetClaim, rule, allowed) -> {
                Claim source = registry.getPrioritizedClaim(event.getBlock());
                if (source != targetClaim) return RuleResult.of(allowed);

                return RuleResult.pass();
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockFromToEvent event);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
