package su.nightexpress.excellentclaims.core.claim.base;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimDefinition;
import su.nightexpress.excellentclaims.api.claim.ClaimIdentity;
import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.claim.ClaimMembers;
import su.nightexpress.excellentclaims.api.claim.ClaimRules;
import su.nightexpress.excellentclaims.api.claim.OwnableClaim;

@NullMarked
public abstract class AbstractOwnedClaim extends AbstractClaim implements OwnableClaim {

    protected ClaimMembers members;

    public AbstractOwnedClaim(ClaimIdentity identity,
                              ClaimDefinition definition,
                              ClaimRules properties,
                              ClaimMembers members) {
        super(identity, definition, properties);
        this.members = members;
    }

    @Override
    public void addMember(ClaimMember member) {
        this.members.addMember(member);
    }

    @Override
    public void addOwner(UUID playerId) {
        this.members.addOwner(playerId);
    }

    @Override
    public void addOwner(Player player) {
        this.addOwner(player.getUniqueId());
    }

    @Override
    public @Nullable ClaimMember getMember(Player player) {
        return this.getMember(player.getUniqueId());
    }

    @Override
    public @Nullable ClaimMember getMember(UUID playerId) {
        return this.members.getMember(playerId);
    }

    @Override
    public Set<ClaimMember> getMembers() {
        return this.members.getMembers();
    }

    @Override
    public Set<UUID> getOwners() {
        return this.members.getOwners();
    }

    @Override
    public boolean isMember(Player player) {
        return this.isMember(player.getUniqueId());
    }

    @Override
    public boolean isMember(UUID playerId) {
        return this.members.hasMember(playerId);
    }

    @Override
    public boolean isOwner(Player player) {
        return this.isOwner(player.getUniqueId());
    }

    @Override
    public boolean isOwner(UUID playerId) {
        return this.members.hasOwner(playerId);
    }

    @Override
    public boolean isOwnerOrMember(Player player) {
        return this.isOwnerOrMember(player.getUniqueId());
    }

    @Override
    public boolean isOwnerOrMember(UUID playerId) {
        return this.isOwner(playerId) || this.isMember(playerId);
    }

    @Override
    public void removeMember(Player player) {
        this.removeMember(player.getUniqueId());
    }

    @Override
    public void removeMember(ClaimMember member) {
        this.members.removeMember(member);
    }

    @Override
    public void removeMember(UUID playerId) {
        this.members.removeMember(playerId);
    }

    @Override
    public void removeMembers() {
        this.members.clearMembers();
    }

    @Override
    public void removeOwner(UUID playerId) {
        this.members.removeOwner(playerId);
    }

    @Override
    public void removeOwner(Player player) {
        this.removeOwner(player.getUniqueId());
    }

    @Override
    public ClaimMembers getMembersDomain() {
        return this.members;
    }
}
