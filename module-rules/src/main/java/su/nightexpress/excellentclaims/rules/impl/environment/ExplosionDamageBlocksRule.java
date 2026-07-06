package su.nightexpress.excellentclaims.rules.impl.environment;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public class ExplosionDamageBlocksRule extends SimpleSpec<BlockExplodeEvent, Boolean> {

    public ExplosionDamageBlocksRule() {
        super(BlockExplodeEvent.class, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
    }

    @Override
    public RuleBehavior<BlockExplodeEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .weight(10)
            .shouldHandle(event -> true)
            .process((event, registry, context) -> {
                // Check source claim first
                if (this.isAnyBlockDenied(registry, context, event.getBlock())) {
                    return RuleResult.deny();
                }

                List<Block> blocks = event.blockList();
                blocks.removeIf(block -> {
                    Claim nextClaim = registry.getPrioritizedClaim(block);
                    if (nextClaim == null) return false;

                    Boolean state = context.resolveValue(nextClaim).orElse(null);
                    return state != null && !state;
                });

                return blocks.isEmpty() ? RuleResult.deny() : RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Explosion Destroy Blocks")
            .description(
                "Controls whether explosions can",
                "destroy blocks here.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.TNT)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
