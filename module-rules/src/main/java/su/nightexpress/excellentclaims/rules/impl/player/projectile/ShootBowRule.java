package su.nightexpress.excellentclaims.rules.impl.player.projectile;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
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

@NullMarked
public class ShootBowRule extends SimpleSpec<EntityShootBowEvent, Boolean> {

    public ShootBowRule() {
        super(EntityShootBowEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
    }

    @Override
    public RuleBehavior<EntityShootBowEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .shouldHandle(event -> event.getEntity() instanceof Player)
            .process((event, registry, context) -> {
                if (!(event.getEntity() instanceof Player)) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(event.getEntity().getLocation());
                if (claim == null) return RuleResult.allow();

                // TODO Claim Permission Check

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isEmpty() || state.get()) return RuleResult.allow();

                ItemStack bow = event.getBow();
                EntityType arrowType = event.getProjectile().getType();

                if (bow == null) {
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_PROJECTILE_SHOOT, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(arrowType)))
                    );
                }
                else {
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_ITEM_USE, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(bow.getType())))
                    );
                }
            })
            .playerExtractor(event -> {
                if (event.getEntity() instanceof Player player) {
                    return player;
                }
                return null;
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Shoot Bows")
            .description(
                "Controls whether outsiders",
                "can shoot bows here."
            )
            .icon(Material.BOW)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return true;
    }
}
