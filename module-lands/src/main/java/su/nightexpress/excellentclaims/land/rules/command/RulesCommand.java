package su.nightexpress.excellentclaims.land.rules.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.excellentclaims.land.rules.ui.LandRuleUIService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class RulesCommand implements CommandExtension {

    private static final String ARG_CLAIM = "claim";

    private final LandCommandResolver    resolver;
    private final LandCommandSuggestions suggestions;
    private final LandRuleUIService      uiService;
    private final MessageDispatcher      dispatcher;

    public RulesCommand(LandCommandResolver resolver,
                        LandCommandSuggestions suggestions,
                        LandRuleUIService uiService,
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
            .description(LandLang.COMMAND_LAND_RULES_DESC)
            .permission(LandPerms.COMMAND_RULES)
            .withArguments(Arguments.argument(ARG_CLAIM, LandClaim.class)
                .optional()
                .suggestions(this.suggestions.memberOnly())
            )
            .executes(this::openRules)
        );
    }

    private boolean openRules(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        LandClaim claim = this.resolver.resolveTarget(context, arguments, ARG_CLAIM);

        ActionResult result = this.uiService.openRules(player, claim);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
