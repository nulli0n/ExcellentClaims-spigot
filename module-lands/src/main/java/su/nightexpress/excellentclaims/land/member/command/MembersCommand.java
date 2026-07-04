package su.nightexpress.excellentclaims.land.member.command;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.MessageDispatcher;
import su.nightexpress.excellentclaims.api.command.CommandExtension;
import su.nightexpress.excellentclaims.api.service.ActionResult;
import su.nightexpress.excellentclaims.land.command.LandCommandResolver;
import su.nightexpress.excellentclaims.land.command.LandCommandSuggestions;
import su.nightexpress.excellentclaims.land.data.model.LandClaim;
import su.nightexpress.excellentclaims.land.lang.LandLang;
import su.nightexpress.excellentclaims.land.member.ui.MemberUIContextFactory;
import su.nightexpress.excellentclaims.land.member.ui.MemberUIService;
import su.nightexpress.excellentclaims.land.permission.LandPerms;
import su.nightexpress.nightcore.commands.Arguments;
import su.nightexpress.nightcore.commands.Commands;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public class MembersCommand implements CommandExtension {

    private static final String ARG_CLAIM = "claim";

    private final LandCommandResolver    resolver;
    private final LandCommandSuggestions suggestions;
    private final MemberUIService        uiService;
    private final MemberUIContextFactory contextFactory;
    private final MessageDispatcher      dispatcher;

    public MembersCommand(LandCommandResolver resolver,
                          LandCommandSuggestions suggestions,
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
            .description(LandLang.COMMAND_LAND_MEMBERS_DESC)
            .permission(LandPerms.COMMAND_MEMBERS)
            .withArguments(Arguments.argument(ARG_CLAIM, LandClaim.class)
                .optional()
                .suggestions(this.suggestions.memberOnly())
            )
            .executes(this::openMembers)
        );
    }

    private boolean openMembers(CommandContext context, ParsedArguments arguments) throws CommandSyntaxException {
        Player player = context.getPlayerOrThrow();
        LandClaim claim = this.resolver.resolveTarget(context, arguments, ARG_CLAIM);

        this.contextFactory.createMembersContext(claim, uiContext -> {
            ActionResult result = this.uiService.showMembersMenu(player, claim, uiContext);
            result.handleFeedback((locale, ctx) -> this.dispatcher.send(locale, player, ctx));
        });

        return true;
    }
}
