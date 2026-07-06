package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleBehavior;
import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.excellentclaims.rules.spec.SimpleSpec;
import su.nightexpress.excellentclaims.rules.type.RuleTypes;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.LangUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public abstract class BasePlayerUseItemRule extends SimpleSpec<PlayerInteractEvent, Boolean> {

    protected final ClaimPermissionAPI permissions;
    protected final int                weight;

    public BasePlayerUseItemRule(ClaimPermissionAPI permissions) {
        this(permissions, 0);
    }

    public BasePlayerUseItemRule(ClaimPermissionAPI permissions, int weight) {
        super(PlayerInteractEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
        this.weight = weight;
    }

    @Override
    public RuleBehavior<PlayerInteractEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .weight(this.weight)
            .shouldHandle(event -> {
                if (event.useItemInHand() == Event.Result.DENY) return false; // Ignore if already denied.

                ItemStack itemStack = event.getItem();
                return itemStack != null && !itemStack.getType().isAir() && this.shouldHandle(event, itemStack);
            })
            .process((event, registry, context) -> {
                ItemStack itemStack = event.getItem();
                if (itemStack == null) return RuleResult.pass();

                Player player = event.getPlayer();
                Location location = player.getLocation();
                if (location == null) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(location);
                if (claim == null) return RuleResult.allow();

                if (this.permissions.hasPermission(player, claim, this.getClaimPermission())) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny(ActionResult.fail(this.getFeedbackLocale(), ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(itemStack.getType()))
                    ));
                }

                return RuleResult.allow();
            })
            .playerExtractor(PlayerInteractEvent::getPlayer)
            .onDeny(event -> event.setUseItemInHand(Result.DENY))
            .build();
    }

    protected MessageLocale getFeedbackLocale() {
        return RulesLang.PROTECTION_ITEM_USE;
    }

    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.ITEM_INTERACT;
    }

    protected abstract boolean shouldHandle(PlayerInteractEvent event, ItemStack itemStack);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
