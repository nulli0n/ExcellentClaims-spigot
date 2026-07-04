package su.nightexpress.excellentclaims.region.editor.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.editor.RegionEditorService;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.LiteralNode;

@NullMarked
public class SetSpawnCommand implements CommandExtension {

    private static final String ARG_REGION = "region";

    private final RegionCommandResolver    resolver;
    private final RegionCommandSuggestions suggestions;
    private final RegionEditorService      editor;
    private final MessageDispatcher        messages;

    public SetSpawnCommand(RegionCommandResolver resolver,
                           RegionCommandSuggestions suggestions,
                           RegionEditorService editor,
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
            .description(RegionLang.COMMAND_REGION_SET_SPAWN_DESC)
            .permission(RegionPerms.COMMAND_SET_SPAWN)
            .withArguments(Arguments.argument(ARG_REGION, RegionClaim.class)
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

        RegionClaim claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.editor.setSpawnLocation(player, claim, location);

        return result.handleFeedback(locale -> {
            this.messages.send(locale, player, ctx -> ctx.with(claim.placeholders()));
        });
    }
}
