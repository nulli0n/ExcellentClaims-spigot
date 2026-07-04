package su.nightexpress.excellentclaims.rank.model;

import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.rank.RankDefinition;

@NullMarked
public class DefaultRankDefinition implements RankDefinition {

    private final String               displayName;
    private final int                  priority;
    private final Set<ClaimPermission> permissions;

    public DefaultRankDefinition(String displayName, int priority, Set<ClaimPermission> permissions) {
        this.displayName = displayName;
        this.priority = priority;
        this.permissions = permissions;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Set<ClaimPermission> getPermissions() {
        return permissions;
    }
}
