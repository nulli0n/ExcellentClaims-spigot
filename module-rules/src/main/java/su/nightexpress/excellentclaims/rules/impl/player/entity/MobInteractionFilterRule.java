package su.nightexpress.excellentclaims.rules.impl.player.entity;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.behavior.base.StandardPlayerInteractEntityHandler;
import su.nightexpress.excellentclaims.rules.filter.FilterMode;
import su.nightexpress.excellentclaims.rules.filter.FilteredSet;
import su.nightexpress.excellentclaims.rules.spec.AbstractFilterSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.BukkitThing;
import su.nightexpress.nightcore.util.bridge.RegistryType;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class MobInteractionFilterRule extends AbstractFilterSpec<PlayerInteractAtEntityEvent, EntityType> {

    private final ClaimPermissionAPI permissions;

    public MobInteractionFilterRule(ClaimPermissionAPI permissions) {
        super(PlayerInteractAtEntityEvent.class, RuleTypes.ENTITY_TYPES, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<PlayerInteractAtEntityEvent, FilteredSet<EntityType>> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .weight(50)
            .allValues(() -> BukkitThing.getAll(RegistryType.ENTITY_TYPE))
            .shouldHandle(event -> true)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getRightClicked().getLocation()))
            .playerExtractor(PlayerInteractAtEntityEvent::getPlayer)
            .trigger(
                new StandardPlayerInteractEntityHandler<FilteredSet<EntityType>>(permissions, ClaimPermission.ENTITY_INTERACT) {

                    @Override
                    protected boolean isEntityAllowed(EntityType type, FilteredSet<EntityType> mobList) {
                        return mobList.isAllowed(type);
                    }

                })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Mob Interactions")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Entity Interact") + " permission",
                "can interact with mobs and entities.",
                "",
                "More specific rules will",
                "override this one."
            )
            .icon(Material.SADDLE)
            .build();
    }

    @Override
    public FilteredSet<EntityType> getDefaultValue() {
        return FilteredSet.empty(FilterMode.WHITELIST);
    }
}
