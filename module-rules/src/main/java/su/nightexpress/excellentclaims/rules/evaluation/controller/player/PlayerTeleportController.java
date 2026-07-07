package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.api.rule.RuleResult;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.player.TeleportContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;

@NullMarked
public class PlayerTeleportController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public PlayerTeleportController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        TeleportCause cause = event.getCause();

        TeleportContext context = new TeleportContext(player, from, to, cause);
        RuleResult result = this.engine.evaluate(context);

        if (result.state() == EventState.DENY) {
            event.setCanCreatePortal(true);

            this.dispatcher.send(RulesLang.PROTECTION_PORTAL_USE, player);
        }
    }
}
