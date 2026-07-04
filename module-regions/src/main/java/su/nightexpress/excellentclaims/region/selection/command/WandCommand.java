package su.nightexpress.excellentclaims.region.selection.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.selection.session.SessionService;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class WandCommand implements CommandExtension {

    private final SessionService    sessionService;
    private final MessageDispatcher dispatcher;

    public WandCommand(SessionService sessionService, MessageDispatcher dispatcher) {
        this.sessionService = sessionService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("wand", builder -> builder
            .playerOnly()
            .description(RegionLang.COMMAND_REGION_WAND_DESC)
            .permission(RegionPerms.COMMAND_REGION_WAND)
            .executes((context, arguments) -> this.run(context))
        );
    }


    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();

        return this.sessionService.toggleSession(player).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
