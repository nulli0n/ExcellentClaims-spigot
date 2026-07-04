package su.nightexpress.excellentclaims.rules.impl.player.damage;

import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.spec.AbstractFilterSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bridge.RegistryType;

@NullMarked
public class PlayerTakeDamageFilterRule extends AbstractFilterSpec<EntityDamageEvent, DamageType> {

    public PlayerTakeDamageFilterRule() {
        super(EntityDamageEvent.class, RuleTypes.DAMAGE_TYPES, RuleCategory.PLAYER);
    }

    @Override
    public RuleBehavior<EntityDamageEvent, FilteredSet<DamageType>> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .allValues(() -> BukkitThing.getAll(RegistryType.DAMAGE_TYPE))
            .shouldHandle(event -> true)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getEntity().getLocation()))
            .trigger((event, registry, claim, rule, damageList) -> {
                if (!(event.getEntity() instanceof Player)) return RuleResult.pass();

                DamageSource source = event.getDamageSource();
                DamageType type = source.getDamageType();
                if (!damageList.isAllowed(type)) {
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
