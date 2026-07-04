package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BlockTrampRule extends BasePlayerUseBlockRule {

    public BlockTrampRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isTrampable(Material material) {
        return material == Material.TURTLE_EGG || material == Material.FARMLAND;
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, Block block) {
        Action action = event.getAction();

        return action == Action.PHYSICAL && this.isTrampable(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Block Trample")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can trample crops & eggs."
            )
            .icon(Material.WHEAT_SEEDS)
            .build();
    }

    @Override
    protected MessageLocale getFeedbackLocale() {
        return RulesLang.PROTECTION_BLOCK_TRAMP;
    }
}
