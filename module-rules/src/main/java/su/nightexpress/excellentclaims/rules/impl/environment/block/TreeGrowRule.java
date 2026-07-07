package su.nightexpress.excellentclaims.rules.impl.environment.block;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.behavior.base.StandardStructureGrowHandler;
import su.nightexpress.excellentclaims.rules.evaluation.context.environment.StructureGrowContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class TreeGrowRule extends SimpleSpec<StructureGrowContext, Boolean> {

    public TreeGrowRule() {
        super(StructureGrowContext.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<StructureGrowContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(event -> {
                TreeType type = event.species();
                return type != TreeType.RED_MUSHROOM && type != TreeType.BROWN_MUSHROOM;
            })
            .process(new StandardStructureGrowHandler())
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Tree Grow")
            .description("Controls whether trees can",
                "grow here."
            )
            .icon(Material.OAK_SAPLING)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
