package su.nightexpress.excellentclaims.wilderness.command;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.wilderness.WildernessRepository;
import su.nightexpress.excellentclaims.wilderness.data.model.WildernessRegion;
import su.nightexpress.excellentclaims.wilderness.lang.WildernessLang;
import su.nightexpress.nightcore.commands.context.CommandContext;
import su.nightexpress.nightcore.commands.context.ParsedArguments;
import su.nightexpress.nightcore.commands.exceptions.CommandSyntaxException;
import su.nightexpress.nightcore.core.config.CoreLang;

@NullMarked
public class WildernessCommandResolver {

    private final WildernessRepository repository;

    public WildernessCommandResolver(WildernessRepository repository) {
        this.repository = repository;
    }

    /**
     * Resolves a Wilderness from the argument, falling back to the player's location.
     * Throws an exception if no claim can be found, halting the command automatically.
     */
    public WildernessRegion resolveTarget(CommandContext context,
                                          ParsedArguments arguments,
                                          String argName) throws CommandSyntaxException {

        if (arguments.contains(argName)) {
            return arguments.get(argName, WildernessRegion.class);
        }

        Player player = context.getPlayer();
        if (player == null) {
            throw CommandSyntaxException.custom(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
        }

        return this.resolveAtLocation(player);
    }

    /**
     * Resolves a Wilderness from the argument, falling back to the player's location.
     * Throws an exception if no claim can be found, halting the command automatically.
     */
    public WildernessRegion resolveAtLocation(Player player) throws CommandSyntaxException {
        Location location = player.getLocation();
        if (location == null) {
            throw CommandSyntaxException.custom(CoreLang.COMMAND_EXECUTION_PLAYER_ONLY);
        }

        WildernessRegion claim = this.repository.getPrioritizedClaim(location);
        if (claim == null) {
            throw CommandSyntaxException.custom(WildernessLang.ERROR_NO_REGION_IN_LOCATION);
        }

        return claim;
    }
}