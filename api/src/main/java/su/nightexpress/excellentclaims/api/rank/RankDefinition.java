package su.nightexpress.excellentclaims.api.rank;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;

@NullMarked
public interface RankDefinition {

    String getDisplayName();

    int getPriority();

    Set<ClaimPermission> getPermissions();
}
