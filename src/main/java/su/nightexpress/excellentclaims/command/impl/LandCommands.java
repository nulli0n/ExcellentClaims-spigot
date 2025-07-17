package su.nightexpress.excellentclaims.command.impl;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.claim.MergeType;
import su.nightexpress.excellentclaims.command.CommandArguments;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.hook.Hooks;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.RootCommand;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;

public class LandCommands {

    public static final String DEF_SETTINGS = "settings";

    private static ClaimPlugin                           plugin;
    private static RootCommand<ClaimPlugin, ChainedNode> command;

    public static void load(@NotNull ClaimPlugin plugin) {
        LandCommands.plugin = plugin;
        String[] aliases = Config.LAND_COMMAND_ALIASES.get();
        if (aliases.length == 0) {
            plugin.error("Could not register Land commands: empty aliases.");
            return;
        }

        command = RootCommand.chained(plugin, aliases, builder -> builder
            .description(Lang.COMMAND_LAND_DESC)
            .permission(Perms.COMMAND_LAND)
        );

        ChainedNode root = command.getNode();

        root.addChildren(DirectNode.builder(plugin, "claim")
            .playerOnly()
            .description(Lang.COMMAND_LAND_CLAIM_DESC)
            .permission(Perms.COMMAND_LAND_CLAIM)
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_NAME)
                .withSamples(context -> plugin.getClaimManager().landLookup().getNames(context.getPlayerOrThrow(), ClaimPermission.ALL)))
            .executes(LandCommands::claimLand)
        );

        root.addChildren(DirectNode.builder(plugin, "unclaim")
            .playerOnly()
            .description(Lang.COMMAND_LAND_UNCLAIM_DESC)
            .permission(Perms.COMMAND_LAND_UNCLAIM)
            .executes(LandCommands::unclaimLand)
        );

        root.addChildren(DirectNode.builder(plugin, "flags")
            .playerOnly()
            .description(Lang.COMMAND_LAND_FLAGS_DESC)
            .permission(Perms.COMMAND_LAND_FLAGS)
            .executes(LandCommands::openFlags)
        );

        root.addChildren(DirectNode.builder(plugin, "members")
            .playerOnly()
            .description(Lang.COMMAND_LAND_MEMBERS_DESC)
            .permission(Perms.COMMAND_LAND_MEMBERS)
            .executes(LandCommands::openMembers)
        );

        root.addChildren(DirectNode.builder(plugin, DEF_SETTINGS)
            .playerOnly()
            .description(Lang.COMMAND_LAND_SETTINGS_DESC)
            .permission(Perms.COMMAND_LAND_SETTINGS)
            .executes(LandCommands::openSettings)
        );

        root.addChildren(DirectNode.builder(plugin, "list")
            .playerOnly()
            .description(Lang.COMMAND_LAND_LIST_DESC)
            .permission(Perms.COMMAND_LAND_LIST)
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).permission(Perms.COMMAND_LAND_LIST_OTHERS))
            .executes(LandCommands::openUserClaims)
        );

        root.addChildren(DirectNode.builder(plugin, "listall")
            .playerOnly()
            .description(Lang.COMMAND_LAND_LIST_ALL_DESC)
            .permission(Perms.COMMAND_LAND_LIST_ALL)
            .withArgument(ArgumentTypes.world(CommandArguments.WORLD))
            .executes(LandCommands::openAllClaims)
        );

        root.addChildren(DirectNode.builder(plugin, "merge")
            .playerOnly()
            .description(Lang.COMMAND_LAND_MERGE_DESC)
            .permission(Perms.COMMAND_LAND_MERGE)
            .executes((context, arguments) -> startMerge(context, arguments, MergeType.MERGE))
        );

        root.addChildren(DirectNode.builder(plugin, "split")
            .playerOnly()
            .description(Lang.COMMAND_LAND_SPLIT_DESC)
            .permission(Perms.COMMAND_LAND_SPLIT)
            .executes((context, arguments) -> startMerge(context, arguments, MergeType.SPLIT))
        );

        root.addChildren(DirectNode.builder(plugin, "setspawn")
            .playerOnly()
            .description(Lang.COMMAND_LAND_SET_SPAWN_DESC)
            .permission(Perms.COMMAND_LAND_SET_SPAWN)
            .executes(LandCommands::setSpawn)
        );

        root.addChildren(DirectNode.builder(plugin, "transfer")
            .playerOnly()
            .description(Lang.COMMAND_LAND_TRANSFER_DESC)
            .permission(Perms.COMMAND_LAND_TRANSFER)
            .withArgument(ArgumentTypes.player(CommandArguments.PLAYER).required())
            .executes(LandCommands::transferOwnership)
        );

        root.addChildren(DirectNode.builder(plugin, "rename")
            .playerOnly()
            .description(Lang.COMMAND_LAND_RENAME_DESC)
            .permission(Perms.COMMAND_LAND_RENAME)
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_NAME).required().complex())
            .executes(LandCommands::rename)
        );

        root.addChildren(DirectNode.builder(plugin, "description")
            .playerOnly()
            .description(Lang.COMMAND_LAND_DESCRIPTION_DESC)
            .permission(Perms.COMMAND_LAND_DESCRIPTION)
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_TEXT).required().complex())
            .executes(LandCommands::setDescription)
        );

        if (Hooks.hasPacketLibrary()) {
            root.addChildren(DirectNode.builder(plugin, "bounds")
                .playerOnly()
                .description(Lang.COMMAND_LAND_BOUNDS_DESC)
                .permission(Perms.COMMAND_LAND_BOUNDS)
                .executes(LandCommands::toggleBounds)
            );
        }

        plugin.getCommandManager().registerCommand(command);
    }

    public static void unload() {
        if (command != null && plugin != null) {
            plugin.getCommandManager().unregisterCommand(command);
            command = null;
            plugin = null;
        }
    }

    public static boolean claimLand(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME, player.getName());
        return plugin.getClaimManager().claimLand(player, player.getLocation(), name);
    }

    public static boolean unclaimLand(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        return plugin.getClaimManager().unclaimChunk(player, player.getLocation());
    }

    public static boolean openFlags(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getMenuManager().openFlagsForCurrentChunk(player);
        return true;
    }

    public static boolean openMembers(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getMenuManager().openMembersForCurrentChunk(player);
        return true;
    }

    public static boolean openSettings(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getMenuManager().openMenuForCurrentChunk(player);
        return true;
    }

    public static boolean openUserClaims(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getUserManager().manageUserSynchronized(arguments.getStringArgument(CommandArguments.PLAYER, player.getName()), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            plugin.getMenuManager().openClaimsMenu(player, ClaimType.CHUNK, UserInfo.of(user));
        });
        return true;
    }

    public static boolean openAllClaims(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        if (arguments.hasArgument(CommandArguments.WORLD)) {
            World world = arguments.getWorldArgument(CommandArguments.WORLD, player.getWorld());
            plugin.getMenuManager().openClaimsMenu(player, ClaimType.CHUNK, world.getName());
        }
        else {
            plugin.getMenuManager().openClaimsMenu(player, ClaimType.CHUNK);
        }
        return true;
    }

    public static boolean setSpawn(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getClaimManager().setSpawnLocation(player, ClaimType.CHUNK);
        return true;
    }

    public static boolean transferOwnership(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Player target = arguments.getPlayerArgument(CommandArguments.PLAYER);
        plugin.getClaimManager().transferOwnership(player, ClaimType.CHUNK, target);
        return true;
    }

    public static boolean startMerge(@NotNull CommandContext context, @NotNull ParsedArguments arguments, @NotNull MergeType type) {
        Player player = context.getPlayerOrThrow();
        plugin.getClaimManager().startMerge(player, type);
        return true;
    }

    public static boolean toggleBounds(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getSelectionManager().toggleChunkBounds(player);
        return true;
    }

    public static boolean rename(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        plugin.getClaimManager().setName(player, ClaimType.CHUNK, name);
        return true;
    }

    public static boolean setDescription(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        plugin.getClaimManager().setDescription(player, ClaimType.CHUNK, name);
        return true;
    }
}
