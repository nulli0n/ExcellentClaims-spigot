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
public class UseAnvilsRule extends BasePlayerUseBlockRule {

    public UseAnvilsRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    private boolean isAnvil(Material material) {
        return Tag.ANVIL.isTagged(material);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, Block block) {
        return this.isAnvil(block.getType());
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Anvils")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Block Interact") + " permission",
                "can use anvils."
            )
            .icon(Material.ANVIL)
            .build();
    }
}
