package su.nightexpress.excellentclaims.rules.impl.player.vehicle;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
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
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BreakVehiclesRule extends SimpleSpec<VehicleDestroyEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BreakVehiclesRule(ClaimPermissionAPI permissions) {
        super(VehicleDestroyEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<VehicleDestroyEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getVehicle().getLocation()))
            .playerExtractor(event -> {
                if (event.getDamageSource().getCausingEntity() instanceof Player player) {
                    return player;
                }
                return null;
            })
            .shouldHandle(event -> true)
            .trigger((event, registry, claim, rule, allowed) -> {
                if (!(event.getDamageSource().getCausingEntity() instanceof Player player)) return RuleResult.pass();

                if (this.permissions.hasPermission(player, claim, ClaimPermission.BUILDING)) {
                    return RuleResult.allow();
                }

                if (!allowed) {
                    EntityType type = event.getVehicle().getType();
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_BLOCK_BREAK, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(type))
                    ));
                }

                return RuleResult.pass();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Destroy Vehicles")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Building") + " permission",
                "can break vehicles here."
            )
            .icon(Material.MINECART)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
