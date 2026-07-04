package su.nightexpress.excellentclaims.api.claim;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.ClaimRule;

@NullMarked
public interface ClaimRules {

    <T> T getOrDefault(ClaimRule<T> property);

    <T> T get(ClaimRule<T> property, T defaultValue);

    <T> boolean has(ClaimRule<T> property);

    <T> void set(ClaimRule<T> property, T value);

    <T> void unset(ClaimRule<T> property);
}
