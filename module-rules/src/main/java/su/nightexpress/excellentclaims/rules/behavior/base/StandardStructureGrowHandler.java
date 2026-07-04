package su.nightexpress.excellentclaims.rules.behavior.base;

import org.bukkit.event.world.StructureGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.ClaimRule;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.HandleTrigger;

@NullMarked
public class StandardStructureGrowHandler implements HandleTrigger<StructureGrowEvent, Boolean> {


    @Override
    public RuleResult handle(StructureGrowEvent event, ClaimRegistry registry, Claim claim, ClaimRule<Boolean> rule,
                             Boolean allowed) {
        if (!allowed) return RuleResult.deny();

        // When a tree trying to grow outside of the current claim.
        // Remove blocks that belongs to other claims, where this rule is disabled.
        event.getBlocks().removeIf(state -> {
            Claim nextClaim = registry.getPrioritizedClaim(state.getLocation());
            if (nextClaim == null || nextClaim == claim) return false;

            Boolean nextResult = nextClaim.getRuleOrIgnoreIfUnset(rule).orElse(null);
            return nextResult != null && !nextResult;
        });

        if (event.getBlocks().isEmpty()) {
            return RuleResult.deny();
        }

        return RuleResult.pass();
    }
}
