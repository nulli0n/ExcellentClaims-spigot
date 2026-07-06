package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.List;
import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BaseEntityExplodeBlocksRule extends SimpleSpec<EntityExplodeEvent, Boolean> {

    public BaseEntityExplodeBlocksRule() {
        super(EntityExplodeEvent.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
    }

    @Override
    public RuleBehavior<EntityExplodeEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> this.shouldHandle(event, event.getEntity()))
            .process((event, registry, context) -> {
                // Check origin claim first (if present)
                Claim sourceClaim = registry.getPrioritizedClaim(event.getLocation());
                Optional<Boolean> sourceState = context.resolveValue(sourceClaim);
                if (sourceState.isPresent() && !sourceState.get()) return RuleResult.deny();

                List<Block> blocks = event.blockList();
                blocks.removeIf(block -> {
                    Claim claim = registry.getPrioritizedClaim(block);
                    if (claim == null) return false;

                    Boolean state = context.resolveValue(claim).orElse(null);
                    return state != null && !state;
                });

                return blocks.isEmpty() ? RuleResult.deny() : RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(EntityExplodeEvent event, Entity entity);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
