package su.nightexpress.excellentclaims.region.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.region.RegionsRepository;
import su.nightexpress.excellentclaims.region.data.model.RegionClaim;
import su.nightexpress.excellentclaims.region.lang.RegionLang;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class RegionCommandResolver {

    private final RegionsRepository repository;

    public RegionCommandResolver(RegionsRepository repository) {
        this.repository = repository;
    }

    /**
     * Resolves a RegionClaim from the argument, falling back to the player's location.
     * Throws an exception if no claim can be found, halting the command automatically.
     */
    public RegionClaim resolveTarget(CommandContext context,
                                     ParsedArguments arguments,
                                     String argName) throws CommandSyntaxException {

        if (arguments.contains(argName)) {
            return arguments.get(argName, RegionClaim.class);
        }

        Player player = context.getPlayer();
        if (player == null) {
            throw CommandSyntaxException.custom(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
        }

        return this.resolveAtLocation(player);
    }

    /**
     * Resolves a RegionClaim from the argument, falling back to the player's location.
     * Throws an exception if no claim can be found, halting the command automatically.
     */
    public RegionClaim resolveAtLocation(Player player) throws CommandSyntaxException {
        Location location = player.getLocation();
        if (location == null) {
            throw CommandSyntaxException.custom(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
        }

        RegionClaim claim = this.repository.getPrioritizedClaim(location);
        if (claim == null) {
            throw CommandSyntaxException.custom(RegionLang.ERROR_NO_REGION_IN_LOCATION);
        }

        return claim;
    }
}