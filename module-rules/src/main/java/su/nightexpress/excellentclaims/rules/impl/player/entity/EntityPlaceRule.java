package su.nightexpress.excellentclaims.rules.impl.player.entity;

import java.util.Optional;

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
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityPlaceContext;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class EntityPlaceRule extends SimpleSpec<EntityPlaceContext, Boolean> {

    private final ClaimPermissionAPI permissions;

    public EntityPlaceRule(ClaimPermissionAPI permissions) {
        super(EntityPlaceContext.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityPlaceContext, Boolean> createBehavior() {
        return this.behaviorBuilder()
            .weight(100)
            .shouldHandle(context -> true)
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
        return RuleDefinition.builder("Place Entities")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Building") + " permission",
                "can place vehicles and",
                "armor stands here."
            )
            .icon(Material.OAK_BOAT)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
