package su.nightexpress.excellentclaims.land.editor.command;

import org.bukkit.Location;
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
import su.nightexpress.nightcore.commands.tree.LiteralNode;

@NullMarked
public class SetSpawnCommand implements CommandExtension {

    private static final String ARG_CLAIM = "claim";

    private final LandCommandResolver    resolver;
    private final LandCommandSuggestions suggestions;
    private final LandEditorService      editor;
    private final MessageDispatcher      messages;

    public SetSpawnCommand(LandCommandResolver resolver,
                           LandCommandSuggestions suggestions,
                           LandEditorService editor,
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
            .description(LandLang.COMMAND_LAND_SET_SPAWN_DESC)
            .permission(LandPerms.COMMAND_SET_SPAWN)
            .withArguments(Arguments.argument(ARG_CLAIM, LandClaim.class)
                .optional()
                .suggestions(this.suggestions.memberOnly())
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

        LandClaim claim = this.resolver.resolveTarget(context, arguments, ARG_CLAIM);

        ActionResult result = this.editor.setSpawnLocation(player, claim, location);

        return result.handleFeedback(locale -> {
            this.messages.send(locale, player, ctx -> ctx.with(claim.placeholders()));
        });
    }
}
