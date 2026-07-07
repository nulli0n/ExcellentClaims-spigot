package su.nightexpress.excellentclaims.rules.behavior.base;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.ClaimRegistry;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleLookup;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior.RuleProcessor;
import su.nightexpress.excellentclaims.rules.evaluation.context.environment.StructureGrowContext;

@NullMarked
public class StandardStructureGrowHandler implements RuleProcessor<StructureGrowContext, Boolean> {

    @Override
    public RuleResult process(StructureGrowContext context, ClaimRegistry registry, RuleLookup<Boolean> resolver) {
        Claim claim = registry.getPrioritizedClaim(context.blockState().getLocation());
        Optional<Boolean> state = resolver.resolveValue(claim);
        if (state.isPresent() && !state.get()) return RuleResult.deny();

        return RuleResult.allow();
    }
}
