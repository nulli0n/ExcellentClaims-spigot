package su.nightexpress.excellentclaims.wilderness.rules.command;

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
import su.nightexpress.excellentclaims.wilderness.rules.ui.RegionRuleUIService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class RulesCommand implements CommandExtension {

    private static final String ARG_WILDERNESS = "wilderness";

    private final WildernessCommandResolver    resolver;
    private final WildernessCommandSuggestions suggestions;
    private final RegionRuleUIService          uiService;
    private final MessageDispatcher            dispatcher;

    public RulesCommand(WildernessCommandResolver resolver,
                        WildernessCommandSuggestions suggestions,
                        RegionRuleUIService uiService,
                        MessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("rules", builder -> builder
            .playerOnly()
            .description(WildernessLang.COMMAND_WILDERNESS_RULES_DESC)
            .permission(WildernessPerms.COMMAND_WILDERNESS_RULES)
            .withArguments(Arguments.argument(ARG_WILDERNESS, WildernessRegion.class)
                .optional()
                .suggestions(this.suggestions.all())
            )
            .executes(this::openRules)
        );
    }

    private boolean openRules(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        WildernessRegion claim = this.resolver.resolveTarget(context, arguments, ARG_WILDERNESS);

        ActionResult result = this.uiService.openRules(player, claim);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
