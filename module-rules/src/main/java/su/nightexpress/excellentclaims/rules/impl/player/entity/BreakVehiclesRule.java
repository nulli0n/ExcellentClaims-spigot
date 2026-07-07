package su.nightexpress.excellentclaims.rules.impl.player.entity;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityRemoveContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BreakVehiclesRule extends SimpleSpec<EntityRemoveContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BreakVehiclesRule(ClaimPermissionAPI permissions) {
        super(EntityRemoveContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityRemoveContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> context.entity() instanceof Vehicle)
            .process((context, registry, resolver) -> {
                Player player = context.actor();

                Claim claim = registry.getPrioritizedClaim(context.entity().getLocation());
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, ClaimPermission.BUILDING)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = resolver.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny();
                }

                return RuleResult.pass();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Destroy Vehicles")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Building") + " permission",
                "can break vehicles here."
            )
            .icon(Material.MINECART)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
