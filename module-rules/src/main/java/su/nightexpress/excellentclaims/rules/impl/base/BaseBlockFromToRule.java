package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFromToEvent;
import org.jspecify.annotations.NullMarked;

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
            .shouldHandle(this::shouldHandle)
            .process((event, registry, context) -> {
                Block targetBlock = event.getToBlock();
                Block sourceBlock = event.getBlock();
                if (this.isAnyBlockDenied(registry, context, targetBlock, sourceBlock)) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(BlockFromToEvent event);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
