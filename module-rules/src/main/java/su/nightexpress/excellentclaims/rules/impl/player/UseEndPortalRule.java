package su.nightexpress.excellentclaims.rules.impl.player;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RuleDefinition;
import su.nightexpress.excellentclaims.rules.impl.base.BasePlayerPortalRule;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class UseEndPortalRule extends BasePlayerPortalRule {

    public UseEndPortalRule(ClaimPermissionAPI permissions) {
        super(permissions);
    }

    @Override
    protected boolean shouldHandle(PlayerPortalEvent event, TeleportCause cause) {
        return cause == TeleportCause.END_PORTAL;
    }

    @Override
    public RuleDefinition getDefaultDefinition() {
        return RuleDefinition.builder("Use End Portals")
            .description(
                "Controls whether outsiders",
                "and members without",
                "the " + TagWrappers.WHITE.wrap("Use Portals") + " permission",
                "can use end portals here."
            )
            .icon(Material.END_PORTAL_FRAME)
            .build();
    }
}
