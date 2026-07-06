package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockSpreadEvent;
import org.jspecify.annotations.NullMarked;

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
            .shouldHandle(this::shouldHandle)
            .process((event, registry, context) -> {
                Block targetBlock = event.getBlock();
                Block sourceBlock = event.getSource();
                if (this.isAnyBlockDenied(registry, context, targetBlock, sourceBlock)) {
                    return RuleResult.deny();
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
