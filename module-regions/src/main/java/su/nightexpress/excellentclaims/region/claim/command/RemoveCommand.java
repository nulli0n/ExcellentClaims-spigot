package su.nightexpress.excellentclaims.region.claim.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.claim.RegionClaimService;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class RemoveCommand implements CommandExtension {

    private static final String ARG_REGION = "region";

    private final RegionCommandSuggestions suggestions;
    private final RegionCommandResolver    resolver;
    private final RegionClaimService       claimService;
    private final MessageDispatcher        dispatcher;

    public RemoveCommand(RegionCommandSuggestions suggestions,
                         RegionCommandResolver resolver,
                         RegionClaimService claimService,
                         MessageDispatcher dispatcher) {
        this.suggestions = suggestions;
        this.resolver = resolver;
        this.claimService = claimService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("remove", builder -> builder
            .playerOnly()
            .description(RegionLang.COMMAND_REGION_UNCLAIM_DESC)
            .permission(RegionPerms.COMMAND_REGION_REMOVE)
            .withArguments(Arguments.argument(ARG_REGION, RegionClaim.class)
                .optional()
                .suggestions(this.suggestions.ownerOnly())
            )
            .executes(this::unclaimLand)
        );
    }

    private boolean unclaimLand(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.claimService.unclaimChunk(player, claim);
        return result.handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
