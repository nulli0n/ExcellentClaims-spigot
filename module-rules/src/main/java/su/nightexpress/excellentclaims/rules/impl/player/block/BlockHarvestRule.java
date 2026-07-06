package su.nightexpress.excellentclaims.rules.impl.player.block;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;
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
public class BlockHarvestRule extends SimpleSpec<PlayerHarvestBlockEvent, Boolean> {

    private final ClaimPermissionAPI permissions;

    public BlockHarvestRule(ClaimPermissionAPI permissions) {
        super(PlayerHarvestBlockEvent.class, RuleTypes.BOOLEAN, RuleCategory.PLAYER);
        this.permissions = permissions;
    }

    @Override
    public RuleBehavior<PlayerHarvestBlockEvent, Boolean> createBehavior() {
        return this.behaviorBuilder(EventPriority.LOW)
            .weight(10)
            .playerExtractor(event -> event.getPlayer())
            .shouldHandle(event -> true)
            .process((event, registry, context) -> {
                Claim claim = registry.getPrioritizedClaim(event.getHarvestedBlock());
                if (claim == null) return RuleResult.allow();

                Player player = event.getPlayer();
                if (this.permissions.hasPermission(player, claim, ClaimPermission.BUILDING)) {
                    return RuleResult.allow();
                }

                Optional<Boolean> state = context.resolveValue(claim);
                if (state.isPresent() && !state.get()) {
                    Block block = event.getHarvestedBlock();
                    return RuleResult.deny(ActionResult.fail(RulesLang.PROTECTION_BLOCK_HARVEST, ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> LangUtil.getSerializedName(block.getType()))
                    ));
                }

                return RuleResult.allow();
            })
            .build();
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Block Harvest")
            .description("Controls whether outsiders and",
                "members without the" + TagWrappers.WHITE.wrap("Building"),
                "claim permission can",
                "harvest crops & plants."
            )
            .icon(Material.SWEET_BERRIES)
            .build();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }
}
