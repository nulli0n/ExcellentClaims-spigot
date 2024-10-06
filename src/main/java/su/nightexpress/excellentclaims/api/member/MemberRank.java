package su.nightexpress.excellentclaims.api.member;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.nightcore.util.placeholder.Placeholder;

import java.util.Set;

public interface MemberRank extends Placeholder {

    @NotNull String getId();

    @NotNull String getDisplayName();

    int getPriority();

    Set<ClaimPermission> getPermissions();

    boolean hasPermission(@NotNull ClaimPermission permission);

    boolean isAbove(@NotNull MemberRank other);

    boolean isBehind(@NotNull MemberRank other);
}
