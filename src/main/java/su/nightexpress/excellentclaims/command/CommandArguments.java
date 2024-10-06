package su.nightexpress.excellentclaims.command;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.ClaimPlugin;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.claim.ClaimType;
import su.nightexpress.excellentclaims.api.claim.RegionClaim;
import su.nightexpress.excellentclaims.config.Lang;
import su.nightexpress.nightcore.command.experimental.CommandContext;
import su.nightexpress.nightcore.command.experimental.argument.CommandArgument;
import su.nightexpress.nightcore.command.experimental.argument.ParsedArguments;
import su.nightexpress.nightcore.command.experimental.builder.ArgumentBuilder;

import java.util.Collections;

public class CommandArguments {

    public static final String PLAYER = "player";
    public static final String NAME = "name";
    public static final String REGION = "region";
    public static final String WORLD = "world";

    @NotNull
    public static ArgumentBuilder<RegionClaim> regionArgument(@NotNull ClaimPlugin plugin) {
        return regionArgument(plugin, null);
    }

    @NotNull
    public static ArgumentBuilder<RegionClaim> regionArgument(@NotNull ClaimPlugin plugin, @Nullable ClaimPermission permission) {
        return CommandArgument.builder(CommandArguments.REGION, (string, context) -> plugin.getClaimManager().getRegionClaim(context.getPlayerOrThrow().getWorld(), string))
            .customFailure(Lang.ERROR_COMMAND_INVALID_REGION_ARGUMENT)
            .localized(Lang.COMMAND_ARGUMENT_NAME_REGION)
            .withSamples(context -> {
                if (permission != null) {
                    Player player = context.getPlayer();
                    if (player == null) return Collections.emptyList();

                    return plugin.getClaimManager().getRegionNames(player, permission);
                }

                return plugin.getClaimManager().getRegionNames(context.getPlayerOrThrow().getWorld());
            });
    }

    @Nullable
    public static RegionClaim getRegionOrAtLocation(@NotNull ClaimPlugin plugin, @NotNull CommandContext context, @NotNull ParsedArguments arguments) {
        if (arguments.hasArgument(REGION)) {
            return arguments.getArgument(REGION, RegionClaim.class);
        }

        Player player = context.getExecutor();
        if (player == null) {
            context.errorPlayerOnly();
            return null;
        }

        Claim claim = plugin.getClaimManager().getPrioritizedClaim(player.getLocation(), ClaimType.REGION);
        if (!(claim instanceof RegionClaim regionClaim)) {
            Lang.ERROR_NO_REGION.getMessage().send(player);
            return null;
        }

        return regionClaim;
    }
}
