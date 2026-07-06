package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.LeavesDecayEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class LeavesDecayRule extends SimpleSpec<LeavesDecayEvent, Boolean> {

    public LeavesDecayRule() {
        super(LeavesDecayEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<LeavesDecayEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> true)
            .process((event, registry, context) -> {
                if (this.isAnyBlockDenied(registry, context, event.getBlock())) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Leaves Decay")
            .description("Controls whether leaves can",
                "decay here."
            )
            .icon(Material.OAK_LEAVES)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
