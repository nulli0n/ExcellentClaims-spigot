package su.nightexpress.excellentclaims.rules.impl.entity.spawn;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
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
public class MobSpawnFilterRule extends AbstractFilterSpec<CreatureSpawnEvent, EntityType> {

    private static final Set<EntityType> SPAWNABLE = BukkitThing.getAll(RegistryType.ENTITY_TYPE)
        .stream()
        .filter(type -> type.isSpawnable() && type.isAlive())
        .collect(Collectors.toSet());

    public MobSpawnFilterRule() {
        super(CreatureSpawnEvent.class, RuleTypes.ENTITY_TYPES, RuleCategory.ENTITY);
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Mob Spawn")
            .description(
                "Controls which mobs",
                "can spawn here.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.SPAWNER)
            .build();
    }

    @Override
    public RuleBehavior<CreatureSpawnEvent, FilteredSet<EntityType>> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .weight(50)
            .allValues(() -> SPAWNABLE)
            .shouldHandle(event -> SPAWNABLE.contains(event.getEntityType()))
            .process((event, claims, context) -> {
                Claim claim = claims.getPrioritizedClaim(event.getLocation());
                FilteredSet<EntityType> mobList = context.resolveValue(claim).orElse(null);
                return mobList == null ? RuleResult.pass() : RuleResult.of(mobList.isAllowed(event.getEntityType()));
            })
            .build();
    }

    @Override
    public FilteredSet<EntityType> getDefaultValue() {
        return FilteredSet.empty(FilterMode.BLACKLIST);
    }
}
