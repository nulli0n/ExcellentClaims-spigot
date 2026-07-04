package su.nightexpress.excellentclaims.api.claim;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface OwnableClaim extends Claim {

    boolean isOwnerOrMember(Player player);

    boolean isOwnerOrMember(UUID playerId);

    boolean isOwner(Player player);

    boolean isOwner(UUID playerId);

    boolean isMember(Player player);

    boolean isMember(UUID playerId);

    void addOwner(Player player);

    void addOwner(UUID playerId);

    void removeOwner(Player player);

    void removeOwner(UUID playerId);

    void addMember(ClaimMember member);

    void removeMember(Player player);

    void removeMember(ClaimMember member);

    void removeMember(UUID playerId);

    void removeMembers();

    Set<UUID> getOwners();

    Set<ClaimMember> getMembers();

    @Nullable
    ClaimMember getMember(Player player);

    @Nullable
    ClaimMember getMember(UUID playerId);

    ClaimMembers getMembersDomain();
}
