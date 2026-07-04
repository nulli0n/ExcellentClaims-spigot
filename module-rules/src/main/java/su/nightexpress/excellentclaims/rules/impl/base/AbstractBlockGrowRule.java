package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockGrowEvent;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class AbstractBlockGrowRule<E extends BlockGrowEvent> extends SimpleSpec<E, Boolean> {

    protected final boolean   resetBlockAge;
    protected final BlockFace growDirection;
    protected final int       weight;

    public AbstractBlockGrowRule(Class<E> type, boolean resetBlockAge, BlockFace growDirection) {
        this(type, resetBlockAge, growDirection, 0);
    }

    public AbstractBlockGrowRule(Class<E> type, boolean resetBlockAge, BlockFace growDirection, int weight) {
        super(type, RuleTypes.BOOLEAN, RuleCategory.NATURAL);
        this.resetBlockAge = resetBlockAge;
        this.growDirection = growDirection;
        this.weight = weight;
    }

    @Override
    public RuleBehavior<E, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .weight(this.weight)
            .claimExtractor((event, registry) -> {
                return registry.getPrioritizedClaim(event.getBlock().getLocation());
            })
            .shouldHandle(this::shouldHandle)
            .trigger((event, registry, claim, rule, allowed) -> {
                // Check target claim first.
                if (!allowed) {
                    this.resetBlockAge(event);
                    return RuleResult.deny();
                }

                // Then check source claim.
                if (this.growDirection != BlockFace.SELF) {
                    Claim sourceClaim = registry.getPrioritizedClaim(this.getSourceBlock(event).getLocation());
                    if (sourceClaim != null && sourceClaim != claim) {
                        Boolean state = sourceClaim.getRuleOrIgnoreIfUnset(rule).orElse(null);
                        if (state == null) return RuleResult.pass();

                        if (!state) {
                            this.resetBlockAge(event);
                        }

                        return RuleResult.of(state);
                    }
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected abstract boolean shouldHandle(@NonNull E event);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }

    protected Block getSourceBlock(@NonNull E event) {
        Block origin = event.getBlock();

        if (this.growDirection != BlockFace.SELF) {
            return origin.getRelative(this.growDirection.getOppositeFace());
        }

        return origin;
    }

    protected void resetBlockAge(@NonNull E event) {
        if (!this.resetBlockAge) return;

        Block source = this.getSourceBlock(event);

        if (source.getBlockData() instanceof Ageable ageable) {
            ageable.setAge(0);
        }
    }
}