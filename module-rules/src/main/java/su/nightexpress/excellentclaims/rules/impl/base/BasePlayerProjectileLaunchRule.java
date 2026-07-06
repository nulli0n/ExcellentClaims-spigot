package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
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
public abstract class BasePlayerProjectileLaunchRule extends SimpleSpec<ProjectileLaunchEvent, Boolean> {

    public BasePlayerProjectileLaunchRule() {
        super(ProjectileLaunchEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
    }

    @Override
    public RuleBehavior<ProjectileLaunchEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> this.shouldHandle(event, event.getEntity()))
            .process((event, registry, context) -> {
                if (!(event.getEntity().getShooter() instanceof Player)) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(event.getLocation());
                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    EntityType type = event.getEntityType();
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_PROJECTILE_SHOOT, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(type)))
                    );
                }

                return RuleResult.allow();
            })
            .playerExtractor(event -> {
                if (event.getEntity().getShooter() instanceof Player player) {
                    return player;
                }
                return null;
            })
            .build();
    }

    protected abstract boolean shouldHandle(ProjectileLaunchEvent event, Projectile projectile);

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
