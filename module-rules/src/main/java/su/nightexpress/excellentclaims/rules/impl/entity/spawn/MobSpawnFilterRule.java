package su.nightexpress.excellentclaims.rules.impl.entity.spawn;

import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntitySpawnEvent;
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
public class MobSpawnFilterRule extends AbstractFilterSpec<EntitySpawnEvent, EntityType> {

    private static final Set<EntityType> SPAWNABLE = BukkitThing.getAll(RegistryType.ENTITY_TYPE)
        .stream()
        .filter(type -> type.isSpawnable())
        .collect(Collectors.toSet());

    public MobSpawnFilterRule() {
        super(EntitySpawnEvent.class, RuleTypes.ENTITY_TYPES, RuleCategory.ENTITY);
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Entity Spawn")
            .description(
                "Controls which entities",
                "can spawn here.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.SPAWNER)
            .build();
    }

    @Override
    public RuleBehavior<EntitySpawnEvent, FilteredSet<EntityType>> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .weight(50)
            .allValues(() -> SPAWNABLE)
            .shouldHandle(event -> SPAWNABLE.contains(event.getEntityType()))
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getLocation()))
            .trigger((event, registry, claim, rule, mobList) -> {
                return RuleResult.of(mobList.isAllowed(event.getEntityType()));
            })
            .build();
    }

    @Override
    public FilteredSet<EntityType> getDefaultValue() {
        return FilteredSet.empty(FilterMode.BLACKLIST);
    }
}
