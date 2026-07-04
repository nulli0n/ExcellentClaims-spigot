package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.List;

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
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getLocation()))
            .trigger((event, registry, claim, rule, allowed) -> {
                if (!allowed) return RuleResult.deny();

                List<Block> blocks = event.blockList();
                blocks.removeIf(block -> {
                    Claim nextClaim = registry.getPrioritizedClaim(block);
                    if (nextClaim == null || nextClaim == claim) return false;

                    Boolean state = nextClaim.getRuleOrIgnoreIfUnset(rule).orElse(null);
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
