package su.nightexpress.excellentclaims.wilderness.editor.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandResolver;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandSuggestions;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.editor.WildernessEditorService;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.excellentclaims.wilderness.permission.WildernessPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class SetNameCommand implements CommandExtension {

    private static final String ARG_REGION = "region";
    private static final String ARG_NAME   = "name";

    private final WildernessCommandResolver    resolver;
    private final WildernessCommandSuggestions suggestions;
    private final WildernessEditorService      editorService;
    private final MessageDispatcher            dispatcher;

    public SetNameCommand(WildernessCommandResolver resolver, WildernessCommandSuggestions suggestions,
                          WildernessEditorService editorService, MessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.editorService = editorService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("rename", builder -> builder
            .playerOnly()
            .description(WildernessLang.COMMAND_WILDERNESS_SET_NAME_DESC)
            .permission(WildernessPerms.COMMAND_SET_NAME)
            .withArguments(
                Arguments.argument(ARG_REGION, WildernessRegion.class)
                    .suggestions(this.suggestions.all()),
                Arguments.greedyString(ARG_NAME).localized(CoreLang.COMMAND_ARGUMENT_NAME_NAME)
            )
            .executes(this::rename)
        );
    }

    private boolean rename(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getString(ARG_NAME);
        WildernessRegion claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.editorService.setName(player, claim, name);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
