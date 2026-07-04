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
public class UseSignsRule extends BasePlayerUseBlockRule {

    public UseSignsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isSign(Material material) {
        return Tag.SIGNS.isTagged(material);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, Block block) {
        return this.isSign(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Signs")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can use signs."
            )
            .icon(Material.OAK_SIGN)
            .build();
    }
}
