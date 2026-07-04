package su.nightexpress.excellentclaims.land.merge.command.impl;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.merge.MergeLang;
import su.nightexpress.excellentclaims.land.merge.session.SessionOrchestrator;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class SplitCommand implements CommandExtension {

    private final LandCommandResolver resolver;
    //private final LandCommandSuggestions    suggestions;
    private final SessionOrchestrator orchestrator;
    private final MessageDispatcher   dispatcher;

    public SplitCommand(LandCommandResolver resolver,
                        LandCommandSuggestions suggestions,
                        SessionOrchestrator orchestrator,
                        MessageDispatcher dispatcher) {
        this.resolver = resolver;
        //this.suggestions = suggestions;
        this.orchestrator = orchestrator;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("split", builder -> builder
            .playerOnly()
            .description(MergeLang.COMMAND_LAND_SPLIT_DESC)
            .permission(LandPerms.COMMAND_SPLIT)
            .executes(this::startSplit)
        );
    }

    private boolean startSplit(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        LandClaim claim = this.resolver.resolveAtLocation(player);

        return this.orchestrator.startSplitting(player, claim).handleFeedback((locale, ctx) -> {
            this.dispatcher.send(locale, player, ctx);
        });
    }
}
