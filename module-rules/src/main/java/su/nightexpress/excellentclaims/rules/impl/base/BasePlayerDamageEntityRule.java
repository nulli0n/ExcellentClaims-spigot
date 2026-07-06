package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public abstract class BasePlayerDamageEntityRule extends SimpleSpec<EntityDamageByEntityEvent, Boolean> {

    protected final ClaimPermissionAPI permissions;

    public BasePlayerDamageEntityRule(ClaimPermissionAPI permissions) {
        super(EntityDamageByEntityEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<EntityDamageByEntityEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .shouldHandle(event -> this.shouldHandle(event, event.getEntity()))
            .process((event, registry, context) -> {
                DamageSource source = event.getDamageSource();
                if (!(source.getCausingEntity() instanceof Player player)) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(event.getEntity().getLocation());
                if (claim == null) return RuleResult.pass();

                ClaimPermission permission = this.getClaimPermission();
                if (permission != null && this.permissions.hasPermission(player, claim, permission)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    EntityType type = event.getEntityType();
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_DAMAGE_ENTITY, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(type))
                    ));
                }

                return RuleResult.allow();
            })
            .playerExtractor(event -> {
                DamageSource source = event.getDamageSource();
                if (source.getCausingEntity() instanceof Player player) return player;

                return null;
            })
            .build();
    }

    protected @Nullable ClaimPermission getClaimPermission() {
        return ClaimPermission.DAMAGE_MOBS;
    }

    protected abstract boolean shouldHandle(EntityDamageByEntityEvent event, Entity entity);
}
