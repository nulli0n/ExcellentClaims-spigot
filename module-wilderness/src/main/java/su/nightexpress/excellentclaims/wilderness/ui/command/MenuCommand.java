package su.nightexpress.excellentclaims.wilderness.ui.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandResolver;
import su.nightexpress.excellentclaims.wilderness.command.WildernessCommandSuggestions;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.excellentclaims.wilderness.permission.WildernessPerms;
import su.nightexpress.excellentclaims.wilderness.ui.WildernessUIService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class MenuCommand implements CommandExtension {

    private static final String ARG_REGION = "region";

    private final WildernessCommandResolver    resolver;
    private final WildernessCommandSuggestions suggestions;
    private final WildernessUIService          uiService;
    private final MessageDispatcher            dispatcher;

    public MenuCommand(WildernessCommandResolver resolver,
                       WildernessCommandSuggestions suggestions,
                       WildernessUIService uiService,
                       MessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("menu", builder -> builder
            .playerOnly()
            .description(WildernessLang.COMMAND_WILDERNESS_MENU_DESC)
            .permission(WildernessPerms.COMMAND_SETTINGS)
            .withArguments(Arguments.argument(ARG_REGION, WildernessRegion.class)
                .optional()
                .suggestions(this.suggestions.all())
            )
            .executes(this::openSettings)
        );
    }

    private boolean openSettings(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        WildernessRegion claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.uiService.showClaimMenu(player, claim);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
