package su.nightexpress.excellentclaims.rules.impl.player.damage;

import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.context.entity.EntityDamageContext;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.spec.AbstractFilterSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bridge.RegistryType;

@NullMarked
public class PlayerTakeDamageFilterRule extends AbstractFilterSpec<EntityDamageContext, DamageType> {

    public PlayerTakeDamageFilterRule() {
        super(EntityDamageContext.class, RuleTypes.DAMAGE_TYPES, RuleCategory.PLAYER);
    }

    @Override
    public RuleBehavior<EntityDamageContext, FilteredSet<DamageType>> createBehavior() {
        return this.behaviorBuilder()
            .allValues(() -> BukkitThing.getAll(RegistryType.DAMAGE_TYPE))
            .shouldHandle(context -> true)
            .process((context, registry, resolver) -> {
                if (!(context.entity() instanceof Player)) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(context.entity().getLocation());
                if (claim == null) return RuleResult.allow();

                FilteredSet<DamageType> damageList = resolver.resolveValue(claim).orElse(null);

                DamageSource source = context.damageSource();
                DamageType type = source.getDamageType();
                if (damageList != null && !damageList.isAllowed(type)) {
                    return RuleResult.deny();
                }

                return RuleResult.pass();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Incoming Player Damage")
            .description(
                "Controls to which damage",
                "types players are vulnerable.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.IRON_SWORD)
            .build();
    }

    @Override
    public FilteredSet<DamageType> getDefaultValue() {
        return FilteredSet.empty(FilterMode.BLACKLIST);
    }
}
