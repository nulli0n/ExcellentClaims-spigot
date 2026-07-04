package su.nightexpress.excellentclaims.api.claim;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ClaimMembers {

    Set<UUID> getOwners();

    Map<UUID, ClaimMember> getMemberMap();

    boolean hasOwner(UUID ownerId);

    boolean hasMember(UUID memberId);

    void clearMembers();

    void addOwner(Player player);

    void addOwner(UUID ownerId);

    void removeOwner(Player player);

    void removeOwner(UUID ownerId);

    Set<ClaimMember> getMembers();

    @Nullable
    ClaimMember getMember(UUID memberId);

    void addMember(ClaimMember member);

    void removeMember(ClaimMember member);

    void removeMember(UUID memberId);
}
