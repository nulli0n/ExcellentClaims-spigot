package su.nightexpress.excellentclaims.rules.handle;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.claim.Claim;

@FunctionalInterface
@NullMarked
public interface PropertyTrigger<E, T> {

    void handle(E event, Claim claim, T value);
}
