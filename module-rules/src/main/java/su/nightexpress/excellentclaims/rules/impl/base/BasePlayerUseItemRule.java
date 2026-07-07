package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemInteractContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;

@NullMarked
public abstract class BasePlayerUseItemRule extends SimpleSpec<ItemInteractContext, Boolean> {

    protected final ClaimPermissionAPI permissions;
    protected final int                weight;

    public BasePlayerUseItemRule(ClaimPermissionAPI permissions) {
        this(permissions, 0);
    }

    public BasePlayerUseItemRule(ClaimPermissionAPI permissions, int weight) {
        super(ItemInteractContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
        this.weight = weight;
    }

    @Override
    public RuleBehavior<ItemInteractContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(this.weight)
            .shouldHandle(event -> this.shouldHandle(event, event.itemStack()))
            .process((event, registry, context) -> {
                Player player = event.actor();
                Location location = player.getLocation();
                if (location == null) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(location);
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, this.getClaimPermission())) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.ITEM_INTERACT;
    }

    protected abstract boolean shouldHandle(ItemInteractContext event, ItemStack itemStack);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
