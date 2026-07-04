package su.nightexpress.excellentclaims.wilderness.editor.command;

import org.bukkit.Location;
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
import su.nightexpress.nightcore.commands.tree.LiteralNode;

@NullMarked
public class SetSpawnCommand implements CommandExtension {

    private static final String ARG_REGION = "region";

    private final WildernessCommandResolver    resolver;
    private final WildernessCommandSuggestions suggestions;
    private final WildernessEditorService      editor;
    private final MessageDispatcher            messages;

    public SetSpawnCommand(WildernessCommandResolver resolver,
                           WildernessCommandSuggestions suggestions,
                           WildernessEditorService editor,
                           MessageDispatcher messages) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.editor = editor;
        this.messages = messages;
    }

    @Override
    public LiteralNode createCommand() {
        return Commands.literal("setspawn", builder -> builder
            .playerOnly()
            .description(WildernessLang.COMMAND_WILDERNESS_SET_SPAWN_DESC)
            .permission(WildernessPerms.COMMAND_SET_SPAWN)
            .withArguments(Arguments.argument(ARG_REGION, WildernessRegion.class)
                .optional()
                .suggestions(this.suggestions.all())
            )
            .executes((context, arguments) -> {
                return this.setSpawn(context, arguments);
            })
        );
    }

    private boolean setSpawn(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        Location location = player.getLocation();
        if (location == null) return false;

        WildernessRegion claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.editor.setSpawnLocation(player, claim, location);

        return result.handleFeedback(locale -> {
            this.messages.send(locale, player, ctx -> ctx.with(claim.placeholders()));
        });
    }
}
