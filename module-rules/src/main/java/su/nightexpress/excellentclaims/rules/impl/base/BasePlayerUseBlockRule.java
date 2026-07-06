package su.nightexpress.excellentclaims.rules.impl.base;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
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
public abstract class BasePlayerUseBlockRule extends SimpleSpec<PlayerInteractEvent, Boolean> {

    protected final ClaimPermissionAPI permissions;
    protected final int                weight;

    public BasePlayerUseBlockRule(ClaimPermissionAPI permissions) {
        this(permissions, 0);
    }

    public BasePlayerUseBlockRule(ClaimPermissionAPI permissions, int weight) {
        super(PlayerInteractEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
        this.weight = weight;
    }

    @Override
    public RuleBehavior<PlayerInteractEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOWEST)
            .weight(this.weight)
            .shouldHandle(event -> {
                if (event.useInteractedBlock() == Event.Result.DENY) return false; // Ignore if already denied.

                Block block = event.getClickedBlock();
                return block != null && !block.isEmpty() && this.shouldHandle(event, block);
            })
            .process((event, registry, context) -> {
                Block block = event.getClickedBlock();
                if (block == null) return RuleResult.pass();

                Claim claim = registry.getPrioritizedClaim(block);
                if (claim == null) return RuleResult.allow();

                Player player = event.getPlayer();
                if (this.permissions.hasPermission(player, claim, this.getClaimPermission())) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    return RuleResult.deny(ActionResult.fail(this.getFeedbackLocale(), ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
                    ));
                }

                return RuleResult.allow();
            })
            .playerExtractor(PlayerInteractEvent::getPlayer)
            .onDeny(event -> event.setUseInteractedBlock(Result.DENY))
            .build();
    }

    protected MessageLocale getFeedbackLocale() {
        return RulesLang.PROTECTION_BLOCK_INTERACT;
    }

    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.BLOCK_INTERACT;
    }

    protected abstract boolean shouldHandle(PlayerInteractEvent event, Block block);

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
