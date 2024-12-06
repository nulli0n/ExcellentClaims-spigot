package su.nightexpress.excellentclaims.command.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.Placeholders;
import su.nightexpress.excellentclaims.command.CommandArguments;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.data.user.ClaimUser;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.impl.ReloadCommand;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;
import su.nightexpress.nightcore.util.CommandUtil;

public class BaseCommands {

    public static void load(@NotNull ClaimPlugin plugin) {
        ChainedNode root = plugin.getRootNode();

        root.addChildren(ReloadCommand.builder(plugin, Perms.COMMAND_RELOAD));

        root.addChildren(DirectNode.builder(plugin, "adminmode")
            .description(Lang.COMMAND_ADMIN_MODE_DESC)
            .permission(Perms.COMMAND_ADMIN_MODE)
            .withArgument(ArgumentTypes.player(CommandArguments.PLAYER))
            .executes((context, arguments) -> setAdminMode(plugin, context, arguments))
        );
    }

    private static boolean setAdminMode(@NotNull ClaimPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = CommandUtil.getPlayerOrSender(context, arguments, CommandArguments.PLAYER);
        if (player == null) {
            context.errorBadPlayer();
            return false;
        }

        ClaimUser user = plugin.getUserManager().getOrFetch(player);
        user.setAdminMode(!user.isAdminMode());
        Lang.ADMIN_MODE_TOGGLE.getMessage().send(player, replacer -> replacer.replace(Placeholders.GENERIC_VALUE, Lang.getEnabledOrDisabled(user.isAdminMode())));
        return true;
    }
}
