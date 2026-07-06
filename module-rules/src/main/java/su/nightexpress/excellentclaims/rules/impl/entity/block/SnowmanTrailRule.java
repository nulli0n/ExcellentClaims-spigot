package su.nightexpress.excellentclaims.rules.impl.entity.block;

import org.bukkit.entity.Snowman;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class SnowmanTrailRule extends SimpleSpec<EntityBlockFormEvent, Boolean> {

    public SnowmanTrailRule() {
        super(EntityBlockFormEvent.class, RuleTypes.BOOLEAN, RuleCategory.ENTITY);
    }

    @Override
    public RuleBehavior<EntityBlockFormEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> event.getEntity() instanceof Snowman)
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
        return RuleDefinition.builder("Snowman Trail")
            .description(
                "Controls whether snowmans",
                "can form snow here."
            )
            .icon(NightItem.asCustomHead("126ab3ed98ff470e4aa03fc69c745f61c0b614f3e1ecb42bac1c929223364789"))
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
