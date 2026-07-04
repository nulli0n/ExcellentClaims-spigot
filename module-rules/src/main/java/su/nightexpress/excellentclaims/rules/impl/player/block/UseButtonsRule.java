package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseButtonsRule extends BasePlayerUseBlockRule {

    public UseButtonsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    // TODO Check Arrow Shooter

    private boolean isButton(Material material) {
        return Tag.BUTTONS.isTagged(material) || material == Material.LEVER;
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, Block block) {
        return this.isButton(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Buttons")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can use buttons."
            )
            .icon(Material.STONE_BUTTON)
            .build();
    }
}
