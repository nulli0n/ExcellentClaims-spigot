package su.nightexpress.excellentclaims.rank;

import java.util.HashSet;
import java.util.Set;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.api.core.id.Identifier;
import su.nightexpress.excellentclaims.api.rank.Rank;
import su.nightexpress.excellentclaims.api.rank.RankDefinition;
import su.nightexpress.excellentclaims.rank.model.DefaultRankDefinition;
import su.nightexpress.nightcore.util.placeholder.PlaceholderResolver;

@NullMarked
public class DefaultRank implements Rank {

    private final Identifier            id;
    private final DefaultRankDefinition definition;

    private final Set<ClaimPermission> effectivePermissions;

    public DefaultRank(Identifier id, DefaultRankDefinition definition) {
        this.id = id;
        this.definition = definition;
        this.effectivePermissions = new HashSet<>(definition.getPermissions());
    }

    @Override
    public PlaceholderResolver placeholders() {
        return RanksPlaceholders.MEMBER_RANK.resolver(this);
    }

    @Override
    public boolean hasPermission(ClaimPermission permission) {
        return this.effectivePermissions.contains(ClaimPermission.ADMIN) || this.effectivePermissions.contains(
            permission);
    }

    @Override
    public void recalculatePermissions(Set<ClaimPermission> permissions) {
        this.effectivePermissions.clear();
        this.effectivePermissions.addAll(permissions);
    }

    @Override
    public boolean isAbove(Rank other) {
        return this.getPriority() > other.getPriority();
    }

    @Override
    public boolean isBehind(Rank other) {
        return this.getPriority() < other.getPriority();
    }

    @Override
    public boolean isEqual(Rank other) {
        return this.getPriority() == other.getPriority();
    }

    @Override
    public RankDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public String getDisplayName() {
        return this.definition.getDisplayName();
    }

    @Override
    public int getPriority() {
        return this.definition.getPriority();
    }

    @Override
    public Set<ClaimPermission> getBasePermissions() {
        return this.definition.getPermissions();
    }

    @Override
    public Set<ClaimPermission> getEffectivePermissions() {
        return this.effectivePermissions;
    }
}
