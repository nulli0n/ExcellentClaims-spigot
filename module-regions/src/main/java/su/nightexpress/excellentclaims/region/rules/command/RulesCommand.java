package su.nightexpress.excellentclaims.region.rules.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.excellentclaims.region.rules.ui.RegionRuleUIService;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class RulesCommand implements CommandExtension {

    private static final String ARG_REGION = "region";

    private final RegionCommandResolver    resolver;
    private final RegionCommandSuggestions suggestions;
    private final RegionRuleUIService      uiService;
    private final MessageDispatcher        dispatcher;

    public RulesCommand(RegionCommandResolver resolver,
                        RegionCommandSuggestions suggestions,
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
            .description(RegionLang.COMMAND_REGION_RULES_DESC)
            .permission(RegionPerms.COMMAND_RULES)
            .withArguments(Arguments.argument(ARG_REGION, RegionClaim.class)
                .optional()
                .suggestions(this.suggestions.memberOnly())
            )
            .executes(this::openRules)
        );
    }

    private boolean openRules(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        ActionResult result = this.uiService.openRules(player, claim);
        return result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
    }
}
