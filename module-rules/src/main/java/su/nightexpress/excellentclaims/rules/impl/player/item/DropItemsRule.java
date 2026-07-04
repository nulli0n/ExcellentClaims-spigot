package su.nightexpress.excellentclaims.rules.impl.player.item;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class DropItemsRule extends SimpleSpec<PlayerDropItemEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public DropItemsRule(ClaimPermissionAPI permissions) {
        super(PlayerDropItemEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<PlayerDropItemEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .weight(20)
            .shouldHandle(event -> true)
            .claimExtractor((event, registry) -> {
                Player player = event.getPlayer();
                Location location = player.getLocation();

                return location == null ? null : registry.getPrioritizedClaim(location);
            })
            .playerExtractor(PlayerDropItemEvent::getPlayer)
            .trigger((event, registry, claim, rule, allowed) -> {
                Player player = event.getPlayer();
                if (this.permissions.hasPermission(player, claim, ClaimPermission.DROP_ITEMS)) {
                    return RuleResult.allow();
                }

                if (!allowed) {
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_ITEM_DROP));
                }

                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Drop Items")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Drop Items") + " permission",
                "can drop items here."
            )
            .icon(Material.STICK)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
