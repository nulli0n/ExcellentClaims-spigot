package su.nightexpress.excellentclaims.land.editor.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.editor.LandEditorService;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class SetNameCommand implements CommandExtension {

    private static final String ARG_CLAIM = "claim";
    private static final String ARG_NAME  = "name";

    private final LandCommandResolver    resolver;
    private final LandCommandSuggestions suggestions;
    private final LandEditorService      editorService;
    private final MessageDispatcher      dispatcher;

    public SetNameCommand(LandCommandResolver resolver, LandCommandSuggestions suggestions,
                          LandEditorService editorService, MessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.editorService = editorService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("rename", builder -> builder
            .playerOnly()
            .description(LandLang.COMMAND_LAND_SET_NAME_DESC)
            .permission(LandPerms.COMMAND_SET_NAME)
            .withArguments(
                Arguments.argument(ARG_CLAIM, LandClaim.class)
                    .suggestions(this.suggestions.memberOnly()),
                Arguments.greedyString(ARG_NAME).localized(CoreLang.COMMAND_ARGUMENT_NAME_NAME)
            )
            .executes(this::rename)
        );
    }

    private boolean rename(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getString(ARG_NAME);
        LandClaim claim = this.resolver.resolveTarget(context, arguments, ARG_CLAIM);

        ActionResult result = this.editorService.setName(player, claim, name);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
