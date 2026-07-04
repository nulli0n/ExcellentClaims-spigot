package su.nightexpress.excellentclaims.api.rank;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.core.id.Identifiable;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolvable;

@NullMarked
public interface Rank extends Identifiable, PlaceholderResolvable {

    String getDisplayName();

    int getPriority();

    Set<ClaimPermission> getBasePermissions();

    Set<ClaimPermission> getEffectivePermissions();

    void recalculatePermissions(Set<ClaimPermission> permissions);

    boolean hasPermission(ClaimPermission permission);

    boolean isAbove(Rank other);

    boolean isBehind(Rank other);

    boolean isEqual(Rank other);
}
