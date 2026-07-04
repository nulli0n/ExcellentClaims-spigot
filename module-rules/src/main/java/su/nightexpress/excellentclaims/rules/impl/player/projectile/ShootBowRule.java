package su.nightexpress.excellentclaims.rules.impl.player.projectile;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

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
            .claimExtractor((event, registry) -> registry.getPrioritizedClaim(event.getEntity().getLocation()))
            .shouldHandle(event -> event.getEntity() instanceof Player)
            .playerExtractor(event -> {
                if (event.getEntity() instanceof Player player) {
                    return player;
                }
                return null;
            })
            .trigger((event, registry, claim, rule, allowed) -> {
                if (!(event.getEntity() instanceof Player)) return RuleResult.pass();

                if (!allowed) {
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
                }

                return RuleResult.allow();
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
