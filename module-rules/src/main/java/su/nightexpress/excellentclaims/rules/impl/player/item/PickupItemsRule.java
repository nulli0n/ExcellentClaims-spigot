package su.nightexpress.excellentclaims.rules.impl.player.item;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PickupItemsRule extends SimpleSpec<EntityPickupItemEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public PickupItemsRule(ClaimPermissionAPI permissions) {
        super(EntityPickupItemEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityPickupItemEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .weight(20)
            .shouldHandle(event -> true)
            .process((event, registry, context) -> {
                if (!(event.getEntity() instanceof Player player)) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(event.getEntity().getLocation());
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, ClaimPermission.PICKUP_ITEMS)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_ITEM_PICKUP));
                }

                return RuleResult.allow();
            })
            .playerExtractor(event -> {
                if (event.getEntity() instanceof Player player) {
                    return player;
                }
                return null;
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
