package su.nightexpress.excellentclaims.rules.impl.player.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.block.data.type.Chest;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseBlockRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class ChestAccessRule extends BasePlayerUseBlockRule {

    public ChestAccessRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractEvent event, Block block) {
        return block.getBlockData() instanceof Chest || block.getBlockData() instanceof Barrel;
    }

    @Override
    protected ClaimPermission getClaimPermission() {
        return ClaimPermission.CHEST_ACCESS;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Chest Access")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Chest Access") + " permission",
                "can access chests."
            )
            .icon(Material.CHEST)
            .build();
    }
}
