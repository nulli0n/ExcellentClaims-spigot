package su.nightexpress.excellentclaims.rules.impl.environment;

import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class FireDamageBlocksRule extends SimpleSpec<BlockBurnEvent, Boolean> {

    public FireDamageBlocksRule() {
        super(BlockBurnEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockBurnEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getBlock()))
            .shouldHandle(event -> true)
            .trigger((event, registry, claim, rule, allowed) -> {
                return RuleResult.of(allowed);
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Fire Damage Blocks")
            .description("Controls whether fire can",
                "destroy blocks."
            )
            .icon(Material.FLINT_AND_STEEL)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
