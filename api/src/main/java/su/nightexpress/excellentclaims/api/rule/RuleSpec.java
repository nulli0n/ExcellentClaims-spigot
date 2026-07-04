package su.nightexpress.excellentclaims.api.rule;

import org.bukkit.event.Event;
import org.jspecify.annotations.NonNull;

public interface RuleSpec<E extends Event, T> {

    @NonNull
    RuleCategory getCategory();

    @NonNull
    RuleType<T> getType();

    @NonNull
    RuleDefinition getDefaultDefinition();

    @NonNull
    T getDefaultValue();

    @NonNull
    RuleBehavior<E, T> createBehavior();
}
