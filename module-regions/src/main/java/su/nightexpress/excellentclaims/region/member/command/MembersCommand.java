package su.nightexpress.excellentclaims.region.member.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.region.command.RegionCommandResolver;
import su.nightexpress.excellentclaims.region.command.RegionCommandSuggestions;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIContextFactory;
import su.nightexpress.excellentclaims.region.member.ui.MemberUIService;
import su.nightexpress.excellentclaims.region.permission.RegionPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class MembersCommand implements CommandExtension {

    private static final String ARG_REGION = "region";

    private final RegionCommandResolver    resolver;
    private final RegionCommandSuggestions suggestions;
    private final MemberUIService          uiService;
    private final MemberUIContextFactory   contextFactory;
    private final MessageDispatcher        dispatcher;

    public MembersCommand(RegionCommandResolver resolver,
                          RegionCommandSuggestions suggestions,
                          MemberUIService uiService,
                          MemberUIContextFactory contextFactory,
                          MessageDispatcher dispatcher) {
        this.resolver = resolver;
        this.suggestions = suggestions;
        this.uiService = uiService;
        this.contextFactory = contextFactory;
        this.dispatcher = dispatcher;
    }

    @Override
    public ExecutableNode createCommand() {
        return Commands.literal("members", builder -> builder
            .playerOnly()
            .description(RegionLang.COMMAND_REGION_MEMBERS_DESC)
            .permission(RegionPerms.COMMAND_MEMBERS)
            .withArguments(Arguments.argument(ARG_REGION, RegionClaim.class)
                .optional()
                .suggestions(this.suggestions.memberOnly())
            )
            .executes(this::openMembers)
        );
    }

    private boolean openMembers(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = this.resolver.resolveTarget(context, arguments, ARG_REGION);

        this.contextFactory.createMembersContext(claim, uiContext -> {
            ActionResult result = this.uiService.showMembersMenu(player, claim, uiContext);
            result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
        });

        return true;
    }
}
