package su.nightexpress.excellentclaims.rules.evaluation.controller.player;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.rule.EventState;
import su.nightexpress.excellentclaims.rules.evaluation.EvaluatorEngine;
import su.nightexpress.excellentclaims.rules.evaluation.context.player.CommandPreProcessContext;
import su.nightexpress.excellentclaims.rules.evaluation.controller.AbstractEventController;
import su.nightexpress.excellentclaims.rules.lang.RulesLang;
import su.nightexpress.nightcore.util.CommandUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class PlayerCommandController extends AbstractEventController {

    private final MessageDispatcher dispatcher;

    public PlayerCommandController(EvaluatorEngine engine, MessageDispatcher dispatcher) {
        super(engine);
        this.dispatcher = dispatcher;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player actor = event.getPlayer();
        String message = event.getMessage();

        String name = CommandUtil.getCommandName(message);
        Command command = CommandUtil.getCommand(name).orElse(null);
        if (command == null) return;

        CommandPreProcessContext context = new CommandPreProcessContext(actor, command);
        if (this.engine.evaluate(context).state() == EventState.DENY) {
            this.dispatcher.send(RulesLang.PROTECTION_COMMAND_USAGE, actor, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> "/" + command.getLabel())
            );
        }
    }
}
