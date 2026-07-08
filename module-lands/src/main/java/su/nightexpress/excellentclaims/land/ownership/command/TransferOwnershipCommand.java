package su.nightexpress.excellentclaims.land.ownership.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.ownership.LandOwnershipService;
import su.nightexpress.excellentclaims.land.ownership.OwnershipLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class TransferOwnershipCommand implements CommandExtension {

    private static final String ARG_CLAIM  = "claim";
    private static final String ARG_PLAYER = "name";

    private final LandCommandResolver    resolver;
    private final LandCommandSuggestions suggestions;
    private final LandOwnershipService   ownership;
    private final MessageDispatcher      dispatcher;

    public TransferOwnershipCommand(LandCommandResolver resolver,
                                    LandCommandSuggestions suggestions,
                                    LandOwnershipService ownership,
                                    MessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.ownership = ownership;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("transfer", builder -> builder
            .playerOnly()
            .description(OwnershipLang.COMMAND_LAND_TRANSFER_DESC)
            .permission(LandPerms.COMMAND_LAND_TRANSFER)
            .withArguments(
                Arguments.player(ARG_PLAYER),
                Arguments.argument(ARG_CLAIM, LandClaim.class)
                    .suggestions(this.suggestions.ownerOnly())
                    .optional()
            )
            .executes(this::transferOwnership)
        );
    }

    private boolean transferOwnership(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        Player target = arguments.getPlayer(ARG_PLAYER);
        LandClaim claim = this.resolver.resolveTarget(context, arguments, ARG_CLAIM);

        ActionResult result = this.ownership.transferOwnership(player, claim, target);
        if (result.success()) {
            this.dispatcher.send(OwnershipLang.TRANSFER_NOTIFY, target, ctx -> ctx
                .with(claim.placeholders())
                .with(CommonPlaceholders.PLAYER.resolver(player))
            );
        }

        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
