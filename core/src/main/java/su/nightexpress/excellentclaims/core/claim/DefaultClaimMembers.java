package su.nightexpress.excellentclaims.core.claim;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.ClaimMember;
import su.nightexpress.excellentclaims.api.claim.ClaimMembers;

@NullMarked
public class DefaultClaimMembers implements ClaimMembers {

    private final Set<UUID>              owners;
    private final Map<UUID, ClaimMember> memberMap;

    public DefaultClaimMembers() {
        this(Set.of(), Map.of());
    }

    public DefaultClaimMembers(Set<UUID> owners, Map<UUID, ClaimMember> members) {
        this.owners = new HashSet<>(owners);
        this.memberMap = new HashMap<>(members);
    }

    public Set<UUID> getOwners() {
        return owners;
    }

    public Map<UUID, ClaimMember> getMemberMap() {
        return memberMap;
    }

    public boolean hasOwner(UUID ownerId) {
        return this.owners.contains(ownerId);
    }

    public boolean hasMember(UUID memberId) {
        return this.memberMap.containsKey(memberId);
    }

    public void clearMembers() {
        this.memberMap.clear();
    }

    public void addOwner(Player player) {
        this.addOwner(player.getUniqueId());
    }

    public void addOwner(UUID ownerId) {
        this.owners.add(ownerId);
    }

    public void removeOwner(Player player) {
        this.removeOwner(player.getUniqueId());
    }

    public void removeOwner(UUID ownerId) {
        this.owners.remove(ownerId);
    }

    public Set<ClaimMember> getMembers() {
        return Set.copyOf(this.memberMap.values());
    }

    public @Nullable ClaimMember getMember(UUID memberId) {
        return this.memberMap.get(memberId);
    }

    public void addMember(ClaimMember member) {
        this.memberMap.put(member.getPlayerId(), member);
    }

    public void removeMember(ClaimMember member) {
        this.removeMember(member.getPlayerId());
    }

    public void removeMember(UUID memberId) {
        this.memberMap.remove(memberId);
    }
}
