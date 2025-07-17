package su.nightexpress.excellentclaims.util;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentclaims.api.claim.Claim;
import su.nightexpress.excellentclaims.api.claim.ClaimPermission;
import su.nightexpress.excellentclaims.flag.impl.ClaimFlag;

import java.util.function.Predicate;

public class Relation {

    private final Claim targetClaim;
    private final Claim sourceClaim;

    private final boolean sameClaim;

    public Relation(@Nullable Claim sourceClaim, @Nullable Claim targetClaim) {
        this.sourceClaim = sourceClaim;
        this.targetClaim = targetClaim;

        this.sameClaim = this.isEmpty() || this.targetClaim == this.sourceClaim;
    }

    public boolean isEmpty() {
        return this.targetClaim == null && this.sourceClaim == null;
    }

    public boolean hasTarget() {
        return this.targetClaim != null;
    }

    public boolean hasSource() {
        return this.sourceClaim != null;
    }

    public boolean isSameClaim() {
        return this.sameClaim;
    }

    @NotNull
    public RelationType getType() {
        return this.getType(null);
    }

    @NotNull
    public RelationType getType(@Nullable Player player) {
        if (this.sourceClaim != null && this.sourceClaim.isWilderness() && this.targetClaim != null && this.targetClaim.isWilderness()) return RelationType.WILDERNESS;

        if (this.sourceClaim != null) {
            if (this.targetClaim == null || this.targetClaim.isWilderness()) return RelationType.TO_WILDERNESS;
            if (this.sourceClaim == this.targetClaim) return RelationType.INSIDE;
            if (player != null && this.isMemberOfBoth(player)) return RelationType.NEIGHIBOR;

            return RelationType.INVADE;
        }

        if (this.targetClaim != null) {
            if (player != null && this.isTargetMember(player)) return RelationType.NEIGHIBOR;

            return RelationType.INVADE;
        }

        return RelationType.WILDERNESS;
    }


    @Deprecated
    public boolean isOwnerOfBoth(@NotNull Player player) {
        return (this.targetClaim == null || this.targetClaim.isOwner(player)) && (this.sourceClaim == null || this.sourceClaim.isOwner(player));
    }

    @Deprecated
    public boolean isMemberOfBoth(@NotNull Player player) {
        return this.isTargetMember(player) && (this.sourceClaim == null || this.sourceClaim.isOwnerOrMember(player));
    }

    @Deprecated
    public boolean isTargetMember(@NotNull Player player) {
        return (this.targetClaim == null || this.targetClaim.isOwnerOrMember(player));
    }


    @Deprecated
    public boolean hasBothPermission(@NotNull Player player, @NotNull ClaimPermission permission) {
        return this.isSameClaim() ? this.hasTargetPermission(player, permission) : this.hasTargetPermission(player, permission) && this.hasSourcePermission(player, permission);
    }

    @Deprecated
    public boolean hasTargetPermission(@NotNull Player player, @NotNull ClaimPermission permission) {
        return this.hasPermission(player, permission, this.targetClaim);
    }

    @Deprecated
    public boolean hasSourcePermission(@NotNull Player player, @NotNull ClaimPermission permission) {
        return this.hasPermission(player, permission, this.sourceClaim);
    }

    @Deprecated
    private boolean hasPermission(@NotNull Player player, @NotNull ClaimPermission permission, @Nullable Claim claim) {
        return claim == null || claim.hasPermission(player, permission);
    }


    @Deprecated
    public boolean checkBothFlag(@NotNull ClaimFlag<Boolean> flag) {
        if (this.isSameClaim()) return this.checkTargetFlag(flag);

        return this.checkTargetFlag(flag) && this.checkSourceFlag(flag);
    }

    @Deprecated
    public boolean checkTargetFlag(@NotNull ClaimFlag<Boolean> flag) {
        return this.checkFlag(flag, this.targetClaim);
    }

    @Deprecated
    public boolean checkSourceFlag(@NotNull ClaimFlag<Boolean> flag) {
        return this.checkFlag(flag, this.sourceClaim);
    }

    @Deprecated
    private boolean checkFlag(@NotNull ClaimFlag<Boolean> flag, @Nullable Claim claim) {
        return claim == null || (claim.isWilderness() && !claim.hasFlag(flag)) || claim.getFlag(flag);
    }


    public boolean testBoth(@NotNull Predicate<Claim> predicate) {
        return this.testClaim(predicate, this.targetClaim) && this.testClaim(predicate, this.sourceClaim);
    }

    public boolean testTarget(@NotNull Predicate<Claim> predicate) {
        return this.testClaim(predicate, this.targetClaim);
    }

    public boolean testSource(@NotNull Predicate<Claim> predicate) {
        return this.testClaim(predicate, this.sourceClaim);
    }

    private boolean testClaim(@NotNull Predicate<Claim> predicate, @Nullable Claim claim) {
        return claim == null || predicate.test(claim);
    }


    public Claim getTargetClaim() {
        return targetClaim;
    }

    public Claim getSourceClaim() {
        return sourceClaim;
    }
}
