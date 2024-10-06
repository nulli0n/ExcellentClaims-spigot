package su.nightexpress.excellentclaims.command.impl;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.command.CommandArguments;
import su.nightexpress.excellentclaims.config.Config;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.excellentclaims.config.Perms;
import su.nightexpress.excellentclaims.util.UserInfo;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.RootCommand;
import su.nightexpress.nightcore.command.experimental.argument.ArgumentTypes;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.node.ChainedNode;
import su.nightexpress.nightcore.command.experimental.node.DirectNode;

public class RegionCommands {

    public static final String DEF_SETTINGS = "settings";

    private static ClaimPlugin plugin;
    private static RootCommand<ClaimPlugin, ChainedNode> command;

    public static void load(@NotNull ClaimPlugin plugin) {
        RegionCommands.plugin = plugin;
        String[] aliases = Config.REGION_COMMAND_ALIASES.get();
        if (aliases.length == 0) {
            plugin.error("Could not register Region commands: empty aliases.");
            return;
        }

        command = RootCommand.chained(plugin, aliases, builder -> builder
            .description(Lang.COMMAND_REGION_DESC)
            .permission(Perms.COMMAND_REGION)
        );

        ChainedNode root = command.getNode();

        root.addChildren(DirectNode.builder(plugin, "wand")
            .playerOnly()
            .description(Lang.COMMAND_REGION_WAND_DESC)
            .permission(Perms.COMMAND_REGION_WAND)
            .executes(RegionCommands::startSelection)
        );

        root.addChildren(DirectNode.builder(plugin, "claim")
            .playerOnly()
            .description(Lang.COMMAND_REGION_CLAIM_DESC)
            .permission(Perms.COMMAND_REGION_CLAIM)
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_NAME).required())
            .executes(RegionCommands::claimRegion)
        );

        root.addChildren(DirectNode.builder(plugin, "remove")
            .playerOnly()
            .description(Lang.COMMAND_REGION_REMOVE_DESC)
            .permission(Perms.COMMAND_REGION_REMOVE)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.REMOVE_CLAIM))
            .executes(RegionCommands::removeRegion)
        );

        root.addChildren(DirectNode.builder(plugin, "flags")
            .playerOnly()
            .description(Lang.COMMAND_REGION_FLAGS_DESC)
            .permission(Perms.COMMAND_REGION_FLAGS)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.MANAGE_FLAGS))
            .executes(RegionCommands::openFlags)
        );

        root.addChildren(DirectNode.builder(plugin, "members")
            .playerOnly()
            .description(Lang.COMMAND_REGION_MEMBERS_DESC)
            .permission(Perms.COMMAND_REGION_MEMBERS)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.VIEW_MEMBERS))
            .executes(RegionCommands::openMembers)
        );

        root.addChildren(DirectNode.builder(plugin, DEF_SETTINGS)
            .playerOnly()
            .description(Lang.COMMAND_REGION_SETTINGS_DESC)
            .permission(Perms.COMMAND_REGION_SETTINGS)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.MANAGE_CLAIM))
            .executes(RegionCommands::openSettings)
        );

        root.addChildren(DirectNode.builder(plugin, "list")
            .playerOnly()
            .description(Lang.COMMAND_REGION_LIST_DESC)
            .permission(Perms.COMMAND_REGION_LIST)
            .withArgument(ArgumentTypes.playerName(CommandArguments.PLAYER).permission(Perms.COMMAND_REGION_LIST_OTHERS))
            .executes(RegionCommands::openUserRegions)
        );

        root.addChildren(DirectNode.builder(plugin, "listall")
            .playerOnly()
            .description(Lang.COMMAND_REGION_DESCRIPTION_DESC)
            .permission(Perms.COMMAND_REGION_DESCRIPTION)
            .withArgument(ArgumentTypes.world(CommandArguments.WORLD))
            .executes(RegionCommands::openAllRegions)
        );

        root.addChildren(DirectNode.builder(plugin, "setspawn")
            .playerOnly()
            .description(Lang.COMMAND_REGION_SET_SPAWN_DESC)
            .permission(Perms.COMMAND_REGION_SET_SPAWN)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.SET_CLAIM_SPAWN))
            .executes(RegionCommands::setSpawn)
        );

        root.addChildren(DirectNode.builder(plugin, "transfer")
            .playerOnly()
            .description(Lang.COMMAND_REGION_TRANSFER_DESC)
            .permission(Perms.COMMAND_REGION_TRANSFER)
            .withArgument(ArgumentTypes.player(CommandArguments.PLAYER).required())
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.TRANSFER_CLAIM))
            .executes(RegionCommands::transferOwnership)
        );

        root.addChildren(DirectNode.builder(plugin, "rename")
            .playerOnly()
            .description(Lang.COMMAND_REGION_RENAME_DESC)
            .permission(Perms.COMMAND_REGION_RENAME)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.RENAME_CLAIM).required())
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_NAME).required().complex())
            .executes(RegionCommands::rename)
        );

        root.addChildren(DirectNode.builder(plugin, "description")
            .playerOnly()
            .description(Lang.COMMAND_REGION_DESCRIPTION_DESC)
            .permission(Perms.COMMAND_REGION_DESCRIPTION)
            .withArgument(CommandArguments.regionArgument(plugin, ClaimPermission.SET_CLAIM_DESCRIPTION).required())
            .withArgument(ArgumentTypes.string(CommandArguments.NAME).localized(Lang.COMMAND_ARGUMENT_NAME_TEXT).required().complex())
            .executes(RegionCommands::setDescription)
        );

        plugin.getCommandManager().registerCommand(command);
    }

    public static void unload() {
        if (command != null && plugin != null) {
            plugin.getCommandManager().unregisterCommand(command);
            command = null;
            plugin = null;
        }
    }

    public static boolean startSelection(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getSelectionManager().startSelection(player);
        return true;
    }

    public static boolean claimRegion(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        return plugin.getClaimManager().claimRegionFromSelection(player, name);
    }

    public static boolean removeRegion(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        return plugin.getClaimManager().removeRegion(player, claim);
    }

    public static boolean openFlags(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getMenuManager().openFlagsMenu(player, claim);
        return true;
    }

    public static boolean openMembers(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getMenuManager().openMembersMenu(player, claim);
        return true;
    }

    public static boolean openSettings(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getMenuManager().openClaimMenu(player, claim);
        return true;
    }

    public static boolean openUserRegions(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        plugin.getUserManager().manageUserSynchronized(arguments.getStringArgument(CommandArguments.PLAYER, player.getName()), user -> {
            if (user == null) {
                context.errorBadPlayer();
                return;
            }

            plugin.getMenuManager().openClaimsMenu(player, ClaimType.REGION, UserInfo.of(user));
        });
        return true;
    }

    public static boolean openAllRegions(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        if (arguments.hasArgument(CommandArguments.WORLD)) {
            World world = arguments.getWorldArgument(CommandArguments.WORLD, player.getWorld());
            plugin.getMenuManager().openClaimsMenu(player, ClaimType.REGION, world.getName());
        }
        else {
            plugin.getMenuManager().openClaimsMenu(player, ClaimType.REGION);
        }
        return true;
    }

    public static boolean setSpawn(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getClaimManager().setSpawnLocation(player, claim, player.getLocation());
        return true;
    }

    public static boolean transferOwnership(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        Player target = arguments.getPlayerArgument(CommandArguments.PLAYER);

        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getClaimManager().transferOwnership(player, claim, target);
        return true;
    }

    public static boolean rename(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getClaimManager().setName(player, claim, name);
        return true;
    }

    public static boolean setDescription(@NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        Player player = context.getPlayerOrThrow();
        String name = arguments.getStringArgument(CommandArguments.NAME);

        RegionClaim claim = CommandArguments.getRegionOrAtLocation(plugin, context, arguments);//arguments.getArgument(CommandArguments.REGION, RegionClaim.class);
        if (claim == null) return false;

        plugin.getClaimManager().setDescription(player, claim, name);
        return true;
    }
}
