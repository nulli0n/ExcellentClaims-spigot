package su.nightexpress.excellentclaims.land.borders.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.land.borders.session.BorderSessionService;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class BordersCommand implements CommandExtension {

    private final BorderSessionService sessionService;
    private final MessageDispatcher    dispatcher;

    public BordersCommand(BorderSessionService sessionService, MessageDispatcher dispatcher) {
        this.sessionService = sessionService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("borders", builder -> builder
            .playerOnly()
            .description(LandLang.COMMAND_LAND_BORDERS_DESC)
            .permission(LandPerms.COMMAND_LAND_BORDERS)
            .executes((context, arguments) -> this.run(context))
        );
    }

    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();

        return this.sessionService.toggleChunkBounds(player).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
