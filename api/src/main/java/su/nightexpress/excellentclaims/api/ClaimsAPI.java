package su.nightexpress.excellentclaims.api;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermissionAPI;
import su.nightexpress.excellentclaims.api.rule.RulesAPI;

@NullMarked
public interface ClaimsAPI {

    /**
     * Retrieves the registry responsible for storing and providing access to all active claims.
     * 
     * @return
     */
    ClaimRegistry getClaimRegistry();

    /**
     * Retrieves the API managing permission nodes, member roles, and access control within claims.
     * 
     * @return
     */
    ClaimPermissionAPI getClaimPermissions();

    /**
     * Retrieves the highest priority claim existing at the specified Block, or null if the block is unclaimed.
     * 
     * @param block
     * @return
     */
    @Nullable
    Claim getPrioritizedClaim(Block block);

    /**
     * Retrieves the highest priority claim existing at the specified Location, or null if the location is unclaimed.
     * 
     * @param location
     * @return
     */
    @Nullable
    Claim getPrioritizedClaim(Location location);

    /**
     * Retrieves an active module instance by its class type, wrapped in an Optional in case the module is disabled or
     * missing.
     * 
     * @param <T>
     * @param moduleType
     * @return
     */
    <T> Optional<T> getModule(Class<T> moduleType);

    /**
     * Retrieves the core rules management API, wrapped in an Optional.
     * 
     * @return
     */
    Optional<RulesAPI> rules();
}
