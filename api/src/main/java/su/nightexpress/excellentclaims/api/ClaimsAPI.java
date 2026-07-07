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

    ClaimRegistry getClaimRegistry();

    ClaimPermissionAPI getClaimPermissions();

    @Nullable
    Claim getPrioritizedClaim(Block block);

    @Nullable
    Claim getPrioritizedClaim(Location location);

    <T> Optional<T> getModule(Class<T> moduleType);

    Optional<RulesAPI> rules();
}
