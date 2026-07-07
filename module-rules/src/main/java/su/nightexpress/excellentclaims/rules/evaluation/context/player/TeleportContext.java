package su.nightexpress.excellentclaims.rules.evaluation.context.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.context.ActionContext;
import su.nightexpress.excellentclaims.api.rule.context.ActorContext;

@NullMarked
public record TeleportContext(Player actor,
                              Location from,
                              Location to,
                              TeleportCause cause) implements ActionContext, ActorContext {

}
