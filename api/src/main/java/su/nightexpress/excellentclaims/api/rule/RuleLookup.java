package su.nightexpress.excellentclaims.api.rule;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.claim.Claim;

@NullMarked
public interface RuleLookup<T> {

    /**
     * Safely resolves the value of the rule for a specific claim.
     * Handles wilderness (or null claim) and unset properties.
     *
     * @param claim The claim to check, or null.
     * @return Optional containing the resolved value, or empty if the rule should pass/be ignored.
     */
    Optional<T> resolveValue(@Nullable Claim claim);
}
