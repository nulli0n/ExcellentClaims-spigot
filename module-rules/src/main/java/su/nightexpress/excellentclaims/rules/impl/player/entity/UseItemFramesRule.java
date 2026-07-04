package su.nightexpress.excellentclaims.rules.impl.player.entity;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerUseEntityRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseItemFramesRule extends BasePlayerUseEntityRule {

    public UseItemFramesRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerInteractAtEntityEvent event, Entity entity) {
        return entity instanceof ItemFrame;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use Item Frames")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Entity Interact") + " permission",
                "can interact with item frames."
            )
            .icon(Material.ITEM_FRAME)
            .build();
    }
}
