package su.nightexpress.excellentclaims.rules.impl.player.hanging;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
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
public class BreakHangingRule extends SimpleSpec<HangingBreakByEntityEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BreakHangingRule(ClaimPermissionAPI permissions) {
        super(HangingBreakByEntityEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<HangingBreakByEntityEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getEntity().getLocation()))
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
                    EntityType type = event.getEntity().getType();
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
        return RuleDefinition.builder("Break Hangings")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Building") + " permission",
                "can break paintings and",
                "item frames here."
            )
            .icon(Material.PAINTING)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
