package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class ContainerAccessRule extends BasePlayerUseBlockRule {

    public ContainerAccessRule(ClaimPermissionAPI permissions) {
        super(permissions, 8);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, Block block) {
        return block.getState() instanceof Container;
    }

    @Override
    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.CONTAINERS;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Container Access")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Container Access") + " permission",
                "can access containers."
            )
            .icon(Material.FURNACE)
            .build();
    }
}
