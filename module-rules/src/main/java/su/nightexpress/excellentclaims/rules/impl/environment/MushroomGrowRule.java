package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.StructureGrowEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.behavior.base.StandardStructureGrowHandler;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class MushroomGrowRule extends SimpleSpec<StructureGrowEvent, Boolean> {

    public MushroomGrowRule() {
        super(StructureGrowEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<StructureGrowEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getLocation()))
            .playerExtractor(StructureGrowEvent::getPlayer)
            .shouldHandle(event -> {
                TreeType type = event.getSpecies();
                return type == TreeType.RED_MUSHROOM || type == TreeType.BROWN_MUSHROOM;
            })
            .trigger(new StandardStructureGrowHandler())
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Mushroom Grow")
            .description("Controls whether mushrooms can",
                "grow here."
            )
            .icon(Material.RED_MUSHROOM)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
