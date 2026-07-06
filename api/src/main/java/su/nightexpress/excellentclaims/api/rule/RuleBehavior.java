package su.nightexpress.excellentclaims.api.rule;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentclaims.api.ClaimRegistry;

@NullMarked
public interface RuleBehavior<E extends Event, T> {

    boolean shouldHandle(E event);

    void denyEvent(E event);

    void allowEvent(E event);

    RuleResult process(E event, ClaimRegistry registry, RuleContext<T> context);

    /* RuleResult handle(E event, ClaimRegistry registry, Claim claim, ClaimRule<T> rule, T value);
    
    @Nullable
    Claim getClaim(E event, ClaimRegistry registry); */

    @Nullable
    Player getUser(E event);

    Class<E> getEventType();

    EventPriority getEventPriority();

    int getWeight();
}
