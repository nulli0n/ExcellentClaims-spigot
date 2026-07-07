package su.nightexpress.excellentclaims.rules.impl.player.entity;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityPlaceContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class PlaceHangingRule extends SimpleSpec<EntityPlaceContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public PlaceHangingRule(ClaimPermissionAPI permissions) {
        super(EntityPlaceContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityPlaceContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .shouldHandle(context -> context.entity() instanceof Hanging)
            .process((context, registry, resolver) -> {
                Claim claim = registry.getPrioritizedClaim(context.block());
                if (claim == null) return RuleResult.allow();

                Player player = context.actor();
                if (this.permissions.hasPermission(player, claim, ClaimPermission.BUILDING)) {
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
        return RuleDefinition.builder("Place Hangings")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Building") + " permission",
                "can place paintings and",
                "item frames here."
            )
            .icon(Material.PAINTING)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
