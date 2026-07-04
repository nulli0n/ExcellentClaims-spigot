package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseBlockSpreadRule extends SimpleSpec<BlockSpreadEvent, Boolean> {

    public BaseBlockSpreadRule() {
        super(BlockSpreadEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockSpreadEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getBlock().getLocation()))
            .shouldHandle(this::shouldHandle)
            .trigger((event, registry, claim, rule, allowed) -> {
                // Check target claim first
                if (!allowed) return RuleResult.deny();

                // Check source claim then
                Claim sourceClaim = registry.getPrioritizedClaim(event.getSource().getLocation());
                if (sourceClaim != null && sourceClaim != claim) {
                    Boolean state = sourceClaim.getRuleOrIgnoreIfUnset(rule).orElse(null);
                    return state == null ? RuleResult.pass() : RuleResult.of(state);
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockSpreadEvent event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
