package su.nightexpress.excellentclaims.rules.spec;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentclaims.api.rule.RuleCategory;
import su.nightexpress.excellentclaims.api.rule.RuleType;
import su.nightexpress.excellentclaims.rules.behavior.AbstractBehavior;

@NullMarked
public abstract class SimpleSpec<E extends Event, T> extends AbstractSpec<E, T> {

    public SimpleSpec(Class<E> eventType, RuleType<T> type, RuleCategory category) {
        super(eventType, type, category);
    }

    public AbstractBehavior.Builder<E, T> behaviorBuilder() {
        return new AbstractBehavior.Builder<>(this.eventType);
    }

    public AbstractBehavior.Builder<E, T> behaviorBuilder(EventPriority priority) {
        return new AbstractBehavior.Builder<>(this.eventType, priority);
    }
}
