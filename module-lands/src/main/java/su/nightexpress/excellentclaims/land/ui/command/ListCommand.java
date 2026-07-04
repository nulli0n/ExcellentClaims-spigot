package su.nightexpress.excellentclaims.land.ui.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.ui.LandUIService;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class ListCommand implements CommandExtension {

    private final LandUIService     uiService;
    private final MessageDispatcher dispatcher;

    public ListCommand(LandUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("list", builder -> builder
            .playerOnly()
            .description(LandLang.COMMAND_LAND_LIST_DESC)
            .permission(LandPerms.COMMAND_LIST)
            .executes((context, arguments) -> this.run(context))
        );
    }

    private boolean run(CommandContext context) {
        Player player = context.getPlayerOrThrow();

        return this.uiService.showClaimListMenu(player).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
