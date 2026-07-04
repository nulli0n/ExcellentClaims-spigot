package su.nightexpress.excellentclaims.region.selection.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.selection.session.SessionService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class ClaimCommand implements CommandExtension {

    private static final String ARG_NAME = "name";

    private final SessionService    sessionService;
    private final MessageDispatcher dispatcher;

    public ClaimCommand(SessionService sessionService,
                        MessageDispatcher dispatcher) {
        this.sessionService = sessionService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("claim", builder -> builder
            .playerOnly()
            .description(RegionLang.COMMAND_REGION_CLAIM_DESC)
            .permission(RegionPerms.COMMAND_CLAIM)
            .withArguments(Arguments.string(ARG_NAME)
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_NAME)
            )
            .executes(this::claimLand)
        );
    }

    private boolean claimLand(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Location location = player.getLocation();
        if (location == null) return false;

        String name = arguments.getString(ARG_NAME);

        return this.sessionService.createRegionFromSelection(player, name).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
