package su.nightexpress.excellentclaims.rules.handle;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.ClaimRule;

@NullMarked
@Deprecated
public record PropertyHandler<E extends Event, T>(ClaimRule<T> property,
                                                  PropertyEventCondition<E> eventCondition,
                                                  PropertyTrigger<E, T> trigger,
                                                  PropertyClaimProvider<E> claimProvider) {
}