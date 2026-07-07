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
public class ExcellentClaimsAPI implements ClaimsAPI {

    private final APIContainer       apiContainer;
    private final ClaimRegistry      claimRegistry;
    private final ClaimPermissionAPI permissions;

    public ExcellentClaimsAPI(APIContainer apiContainer) {
        this.apiContainer = apiContainer;
        this.claimRegistry = apiContainer.container().get(ClaimRegistry.class);
        this.permissions = apiContainer.container().get(ClaimPermissionAPI.class);
    }

    @Override
    public ClaimRegistry getClaimRegistry() {
        return this.claimRegistry;
    }

    @Override
    public ClaimPermissionAPI getClaimPermissions() {
        return this.permissions;
    }

    @Override
    public <T> Optional<T> getModule(Class<T> moduleType) {
        return this.apiContainer.container().lookup(moduleType);
    }

    @Override
    public @Nullable Claim getPrioritizedClaim(Block block) {
        return this.claimRegistry.getPrioritizedClaim(block);
    }

    @Override
    public @Nullable Claim getPrioritizedClaim(Location location) {
        return this.getPrioritizedClaim(location);
    }

    public Optional<RulesAPI> rules() {
        return this.getModule(RulesAPI.class);
    }
}
