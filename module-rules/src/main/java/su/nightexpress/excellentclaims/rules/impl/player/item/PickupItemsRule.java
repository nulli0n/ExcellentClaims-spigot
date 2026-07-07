package su.nightexpress.excellentclaims.rules.impl.player.item;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.item.ItemPickupContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PickupItemsRule extends SimpleSpec<ItemPickupContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public PickupItemsRule(ClaimPermissionAPI permissions) {
        super(ItemPickupContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<ItemPickupContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(20)
            .shouldHandle(context -> true)
            .process((context, registry, resolver) -> {
                Player player = context.actor();
                Location location = player.getLocation();
                if (location == null) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(location);
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, ClaimPermission.PICKUP_ITEMS)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = resolver.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Pickup Items")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Pickup Items") + " permission",
                "can pickup items here."
            )
            .icon(Material.HOPPER)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
