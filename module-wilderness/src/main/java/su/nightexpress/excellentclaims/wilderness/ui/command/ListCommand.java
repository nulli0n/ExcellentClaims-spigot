package su.nightexpress.excellentclaims.wilderness.ui.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.excellentclaims.wilderness.permission.WildernessPerms;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class ListCommand implements CommandExtension {

    private final WildernessUIService uiService;
    private final MessageDispatcher   dispatcher;

    public ListCommand(WildernessUIService uiService, MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("list", builder -> builder
            .playerOnly()
            .description(WildernessLang.COMMAND_WILDERNESS_LIST_DESC)
            .permission(WildernessPerms.COMMAND_LIST)
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
