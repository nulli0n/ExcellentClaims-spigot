package su.nightexpress.excellentclaims.land.claim.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.claim.LandClaimService;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class ClaimCommand implements CommandExtension {

    private static final String ARG_NAME = "name";

    private final LandCommandSuggestions suggestions;
    private final LandClaimService       claimService;
    private final MessageDispatcher      dispatcher;

    public ClaimCommand(LandCommandSuggestions suggestions,
                        LandClaimService claimService,
                        MessageDispatcher dispatcher) {
        this.suggestions = suggestions;
        this.claimService = claimService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("claim", builder -> builder
            .playerOnly()
            .description(LandLang.COMMAND_LAND_CLAIM_DESC)
            .permission(LandPerms.COMMAND_LAND_CLAIM)
            .withArguments(Arguments.string(ARG_NAME)
                .optional()
                .localized(CoreLang.COMMAND_ARGUMENT_NAME_NAME)
                .suggestions(this.suggestions.ownerOnly())
            )
            .executes(this::claimLand)
        );
    }

    private boolean claimLand(CommandContext context, ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Location location = player.getLocation();
        if (location == null) return false;

        String name = arguments.contains(ARG_NAME) ? arguments.getString(ARG_NAME) : null;
        ActionResult result = this.claimService.claimLand(player, location, name);

        return result.handleFeedback((locale, extra) -> {
            this.dispatcher.send(locale, player, builder -> builder.apply(extra));
        });
    }
}
