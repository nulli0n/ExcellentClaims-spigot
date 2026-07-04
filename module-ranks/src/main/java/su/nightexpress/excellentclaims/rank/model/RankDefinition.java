package su.nightexpress.excellentclaims.rank.model;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;

@NullMarked
public class RankDefinition {

    private final String               displayName;
    private final int                  priority;
    private final Set<ClaimPermission> permissions;

    public RankDefinition(String displayName, int priority, Set<ClaimPermission> permissions) {
        this.displayName = displayName;
        this.priority = priority;
        this.permissions = permissions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriority() {
        return priority;
    }

    public Set<ClaimPermission> getPermissions() {
        return permissions;
    }
}
