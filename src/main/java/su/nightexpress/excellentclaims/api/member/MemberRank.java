package su.nightexpress.excellentclaims.api.member;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;

import java.util.Set;
import java.util.function.UnaryOperator;

public interface MemberRank {

    @NotNull UnaryOperator<String> replacePlaceholders();

    @NotNull String getId();

    @NotNull String getDisplayName();

    int getPriority();

    Set<ClaimPermission> getPermissions();

    boolean hasPermission(@NotNull ClaimPermission permission);

    boolean isAbove(@NotNull MemberRank other);

    boolean isBehind(@NotNull MemberRank other);
}
