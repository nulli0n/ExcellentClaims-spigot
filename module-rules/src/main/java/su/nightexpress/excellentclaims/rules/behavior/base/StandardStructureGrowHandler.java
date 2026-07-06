package su.nightexpress.excellentclaims.rules.behavior.base;

import java.util.Optional;

import org.bukkit.event.world.StructureGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleContext;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.RuleProcessor;

@NullMarked
public class StandardStructureGrowHandler implements RuleProcessor<StructureGrowEvent, Boolean> {

    @Override
    public RuleResult process(StructureGrowEvent event, ClaimRegistry registry, RuleContext<Boolean> context) {
        // Check origin claim first (if present only)
        Claim origin = registry.getPrioritizedClaim(event.getLocation());
        Optional<Boolean> originState = context.resolveValue(origin);
        if (originState.isPresent() && !originState.get()) return RuleResult.deny();

        // When a tree trying to grow outside of the current claim.
        // Remove blocks that belongs to other claims, where this rule is disabled.
        event.getBlocks().removeIf(state -> {
            Claim nextClaim = registry.getPrioritizedClaim(state.getLocation());
            if (nextClaim == null || nextClaim == origin) return false;

            Optional<Boolean> nextState = context.resolveValue(nextClaim);
            return nextState.isPresent() && !nextState.get();
        });

        if (event.getBlocks().isEmpty()) {
            return RuleResult.deny();
        }

        return RuleResult.allow();
    }
}
