package su.nightexpress.excellentclaims.land.ui.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.ui.LandUIContextFactory;
import su.nightexpress.excellentclaims.land.ui.LandUIService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class InspectCommand implements CommandExtension {

    private static final String ARG_PLAYER = "player";

    private final LandUIService        uiService;
    private final LandUIContextFactory uiContextFactory;
    private final MessageDispatcher    dispatcher;

    public InspectCommand(LandUIService uiService,
                          LandUIContextFactory uiContextFactory,
                          MessageDispatcher dispatcher) {
        this.uiService = uiService;
        this.uiContextFactory = uiContextFactory;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("inspect", builder -> builder
            .playerOnly()
            .description(LandLang.COMMAND_LAND_INSPECT_DESC)
            .permission(LandPerms.COMMAND_LAND_INSPECT)
            .withArguments(Arguments.playerName(ARG_PLAYER))
            .executes(this::run)
        );
    }

    private boolean run(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String playerName = arguments.getString(ARG_PLAYER);
        if (playerName.equals(player.getName())) {
            return this.uiService.showClaimListMenu(player).handleFeedback((locale, ctx) -> {
                this.dispatcher.send(locale, player, ctx);
            });
        }

        this.uiContextFactory.createInspectContext(playerName, opt -> {
            if (opt.isEmpty()) {
                this.dispatcher.send(CoreLang.ERROR_INVALID_PLAYER, player);
                return;
            }

            this.uiService.showClaimInspectMenu(player, opt.get()).handleFeedback((locale, ctx) -> {
                this.dispatcher.send(locale, player, ctx);
            });
        });

        return true;
    }
}
