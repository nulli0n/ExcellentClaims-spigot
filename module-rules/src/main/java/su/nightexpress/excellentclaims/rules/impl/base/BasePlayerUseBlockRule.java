package su.nightexpress.excellentclaims.rules.impl.base;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.rules.behavior.base.StandardPlayerInteractBlockHandler;
import su.nightexpress.excellentclaims.rules.evaluation.context.block.BlockInteractContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerUseBlockRule extends SimpleSpec<BlockInteractContext, Boolean> {

    protected final ClaimPermissionAPI permissions;
    protected final int                weight;

    public BasePlayerUseBlockRule(ClaimPermissionAPI permissions) {
        this(permissions, 0);
    }

    public BasePlayerUseBlockRule(ClaimPermissionAPI permissions, int weight) {
        super(BlockInteractContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
        this.weight = weight;
    }

    @Override
    public RuleBehavior<BlockInteractContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(this.weight)
            .shouldHandle(context -> this.shouldHandle(context, context.block()))
            .process(new StandardPlayerInteractBlockHandler<Boolean>(this.permissions, this.getClaimPermission()) {

                @Override
                protected boolean isBlockAllowed(Material type, Boolean allowed) {
                    return allowed;
                }

            })
            .build();
    }

    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.BLOCK_INTERACT;
    }

    protected abstract boolean shouldHandle(BlockInteractContext event, Block block);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
