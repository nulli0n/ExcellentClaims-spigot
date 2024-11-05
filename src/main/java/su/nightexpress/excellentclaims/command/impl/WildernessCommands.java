package su.nightexpress.excellentclaims.command.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.claim.impl.Wilderness;
import su.nightexpress.excellentclaims.command.CommandArguments;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.RootCommand;
import su.nightexpress.nightcore.command.experimental.ServerCommand;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;

public class WildernessCommands {

    private static ClaimPlugin   plugin;
    private static ServerCommand command;

    public static void load(@NotNull ClaimPlugin plugin) {
        WildernessCommands.plugin = plugin;
        String[] aliases = Config.WILDERNESS_COMMAND_ALIASES.get();
        if (aliases.length == 0) {
            plugin.error("Could not register Wilderness commands: empty aliases.");
            return;
        }

        var rootCommand = RootCommand.chained(plugin, aliases, builder -> builder
            .description(Lang.COMMAND_WILDERNESS_DESC)
            .permission(Perms.COMMAND_WILDERNESS)
        );

        ChainedNode root = rootCommand.getNode();

        root.addChildren(DirectNode.builder(plugin, "flags")
            .playerOnly()
            .description(Lang.COMMAND_WILDERNESS_FLAGS_DESC)
            .permission(Perms.COMMAND_WILDERNESS_FLAGS)
            .withArgument(CommandArguments.forWilderness(plugin))
            .executes(WildernessCommands::openFlags)
        );

        root.addChildren(DirectNode.builder(plugin, "rename")
            .playerOnly()
            .description(Lang.COMMAND_WILDERNESS_RENAME_DESC)
            .permission(Perms.COMMAND_WILDERNESS_RENAME)
            .withArgument(CommandArguments.forWilderness(plugin).required())
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_NAME).required().complex())
            .executes(WildernessCommands::rename)
        );

        root.addChildren(DirectNode.builder(plugin, "description")
            .playerOnly()
            .description(Lang.COMMAND_WILDERNESS_DESCRIPTION_DESC)
            .permission(Perms.COMMAND_WILDERNESS_DESCRIPTION)
            .withArgument(CommandArguments.forWilderness(plugin).required())
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_TEXT).required().complex())
            .executes(WildernessCommands::setDescription)
        );

        plugin.getCommandManager().registerCommand(command = rootCommand);
    }

    public static void unload() {
        if (command != null && plugin != null) {
            plugin.getCommandManager().unregisterCommand(command);
            command = null;
            plugin = null;
        }
    }

    public static boolean openFlags(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Wilderness claim = CommandArguments.getWildernessOrAtLocation(plugin, context, arguments);
        if (claim == null) return false;

        plugin.getMenuManager().openFlagsMenu(player, claim);
        return true;
    }

    public static boolean rename(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        Wilderness claim = CommandArguments.getWildernessOrAtLocation(plugin, context, arguments);
        if (claim == null) return false;

        plugin.getClaimManager().setName(player, claim, name);
        return true;
    }

    public static boolean setDescription(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        Wilderness claim = CommandArguments.getWildernessOrAtLocation(plugin, context, arguments);
        if (claim == null) return false;

        plugin.getClaimManager().setDescription(player, claim, name);
        return true;
    }
}
