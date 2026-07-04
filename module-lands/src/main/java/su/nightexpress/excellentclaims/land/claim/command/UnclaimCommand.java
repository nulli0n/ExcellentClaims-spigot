package su.nightexpress.excellentclaims.land.claim.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.claim.LandClaimService;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class UnclaimCommand implements CommandExtension {

    private static final String ARG_CLAIM = "claim";

    private final LandCommandSuggestions suggestions;
    private final LandCommandResolver    resolver;
    private final LandClaimService       claimService;
    private final MessageDispatcher      dispatcher;

    public UnclaimCommand(LandCommandSuggestions suggestions, LandCommandResolver resolver,
                          LandClaimService claimService, MessageDispatcher dispatcher) {
        this.suggestions = suggestions;
        this.resolver = resolver;
        this.claimService = claimService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("unclaim", builder -> builder
            .playerOnly()
            .description(LandLang.COMMAND_LAND_UNCLAIM_DESC)
            .permission(LandPerms.COMMAND_UNCLAIM)
            .withArguments(Arguments.argument(ARG_CLAIM, LandClaim.class)
                .optional()
                .suggestions(this.suggestions.ownerOnly())
            )
            .executes(this::unclaimLand)
        );
    }

    private boolean unclaimLand(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        LandClaim claim = this.resolver.resolveTarget(context, arguments, ARG_CLAIM);

        ActionResult result = this.claimService.unclaimChunk(player, claim);
        return result.handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
