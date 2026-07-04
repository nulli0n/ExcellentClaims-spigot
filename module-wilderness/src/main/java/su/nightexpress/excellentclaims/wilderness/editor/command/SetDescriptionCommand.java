package su.nightexpress.excellentclaims.wilderness.editor.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.core.Lang;
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

@NullMarked
public class SetDescriptionCommand implements CommandExtension {

    private static final String ARG_REGION = "region";
    private static final String ARG_NAME   = "name";

    private final WildernessCommandResolver    resolver;
    private final WildernessCommandSuggestions suggestions;
    private final WildernessEditorService      editor;
    private final MessageDispatcher            dispatcher;

    public SetDescriptionCommand(WildernessCommandResolver resolver,
                                 WildernessCommandSuggestions suggestions,
                                 WildernessEditorService editor,
                                 MessageDispatcher messages) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.editor = editor;
        this.dispatcher = messages;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("description", builder -> builder
            .playerOnly()
            .description(WildernessLang.COMMAND_WILDERNESS_SET_DESCRIPTION_DESC)
            .permission(WildernessPerms.COMMAND_SET_DESCRIPTION)
            .withArguments(
                Arguments.argument(ARG_REGION, WildernessRegion.class)
                    .suggestions(this.suggestions.all()),
                Arguments.greedyString(ARG_NAME).localized(Lang.COMMAND_ARGUMENT_NAME_TEXT)
            )
            .executes(this::setDescription)
        );
    }

    private boolean setDescription(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getString(ARG_NAME);
        WildernessRegion claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.editor.setDescription(player, claim, name);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
